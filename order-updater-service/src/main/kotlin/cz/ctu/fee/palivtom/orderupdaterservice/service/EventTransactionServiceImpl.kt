package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.config.EVENT_TASK_EXECUTOR
import cz.ctu.fee.palivtom.orderupdaterservice.model.EventList
import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces.EventProcessor
import cz.ctu.fee.palivtom.orderupdaterservice.producer.TransactionStatusProducer
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val logger = KotlinLogging.logger {}

@Service
class EventTransactionServiceImpl(
    private val transactionStatusProducer: TransactionStatusProducer,
    private val eventProcessor: EventProcessor,
    @Qualifier(EVENT_TASK_EXECUTOR) private val eventExecutor: ThreadPoolTaskExecutor
) : EventTransactionService {

    private val locks = HashMap<String, ReentrantLock>()
    private val transactions = ConcurrentHashMap<String, EventList>()
    private val queue = LinkedList<String>()

    @Synchronized
    override fun registerTransaction(transaction: Transaction) {
        getLock(transaction.id).withLock {
            logger.info { "Registering transaction: $transaction" }

            if (transactions.containsKey(transaction.id)) {
                if (transactions[transaction.id]!!.isCountSet()) {
                    return logger.error { "Transaction '${transaction.id}' already registered." }
                }
                transactions[transaction.id]!!.setTargetCount(transaction.supportedEventCount)
            } else {
                transactions[transaction.id] = EventList(transaction.supportedEventCount)
            }
        }
        queue.add(transaction.id)
        processQueue()
    }

    override fun addEventToTransaction(event: Event) {
        val txId = event.eventMetadata.txId
        getLock(txId).withLock {
            logger.info { "Adding event ${event.eventMetadata} to transaction '$txId'." }

            val eventList = transactions.getOrPut(txId) { EventList() }
            eventList.addEvent(event)
        }
        processQueue()
    }

    @Synchronized
    private fun getLock(txId: String): ReentrantLock {
        return locks.getOrPut(txId) { ReentrantLock() }
    }

    private fun pollEvents(txId: String): List<Event> {
        val eventList = transactions[txId]!!
        locks.remove(txId)
        transactions.remove(txId)
        return eventList.getEvents()
    }

    @Synchronized
    private fun processQueue() {
        val peekValue = queue.peek() ?: return
        val eventList = transactions[peekValue]
        if (eventList != null && eventList.isComplete()) {
            val pollValue = queue.poll()
            val pollEvents = pollEvents(pollValue)
            eventExecutor.execute { processEvents(pollValue, pollEvents) }

            processQueue()
        }
    }

    @Transactional
    protected fun processEvents(txId: String, events: List<Event>) {
        try {
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.BEGIN)
            events
                .sortedBy { it.eventMetadata.txTotalOrder }
                .forEach { it.accept(eventProcessor) }
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.SUCCESS)
        } catch (e: Exception) {
            logger.error("Error while processing transaction '$txId'.", e)
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.FAIL)
        }
    }
}
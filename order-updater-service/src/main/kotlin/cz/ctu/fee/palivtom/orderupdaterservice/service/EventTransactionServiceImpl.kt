package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.EventList
import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.orderupdaterservice.producer.TransactionStatusProducer
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
import cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces.EventProcessor
import mu.KotlinLogging
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.HashMap
import kotlin.concurrent.withLock

@Service
class EventTransactionServiceImpl(
    private val transactionStatusProducer: TransactionStatusProducer,
    private val eventProcessor: EventProcessor
) : EventTransactionService {
    private val logger = KotlinLogging.logger {}

    private val taskExecutor = SyncTaskExecutor()
    private val transactions = HashMap<String, EventList>()
    private val locks = ConcurrentHashMap<String, ReentrantLock>()

    override fun registerTransaction(transaction: Transaction) {
        val lock = locks.getOrPut(transaction.id) { ReentrantLock() }
        lock.withLock {
            logger.info { "Registering transaction: ${transaction.id}" }

            if (transactions[transaction.id] == null) {
                transactions[transaction.id] = EventList(transaction.eventCount)
            } else {
                if (transactions[transaction.id]!!.isCountSet()) {
                    logger.error { "Transaction ${transaction.id} already registered." }
                    return
                }

                val isCollected = transactions[transaction.id]!!.setTargetCount(transaction.eventCount)
                if (isCollected) {
                    executeEvents(transaction.id)
                }
            }
        }
    }

    override fun addEventToTransaction(event: Event) {
        val txId = event.eventMetadata.txId
        val lock = locks.getOrPut(txId) { ReentrantLock() }
        lock.withLock {
            logger.info { "Adding event ${event.eventMetadata} to transaction $txId" }

            val eventList = transactions.getOrPut(txId) { EventList() }
            val isCollected = eventList.addEvent(event)
            if (isCollected) {
                executeEvents(txId)
            }
        }
    }

    private fun executeEvents(txId: String) {
        transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.BEGIN)
        // TODO what about transaction order? - only in case of scaling
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.PROCESSING)

            val events = transactions[txId]!!.getEvents()
            transactions.remove(txId)
            locks.remove(txId)

            taskExecutor.execute {
                proceedEvents(txId, events)
            }
        }

        @Transactional
        protected fun proceedEvents(txId: String, events: List<Event>) {
            try {
                events
                    .sortedBy { it.eventMetadata.txTotalOrder }
                    .forEach { it.accept(eventProcessor) }
                logger.warn { "Transaction $txId processed successfully." }
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.SUCCESS)
        } catch (e: Exception) { // todo what exception?
            logger.error { "Error while processing transaction $txId" }
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.FAIL)
        }
    }
}
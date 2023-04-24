package cz.ctu.fee.palivtom.transactiongrouperservice.service.impl

import cz.ctu.fee.palivtom.transactiongrouperservice.config.TRANSACTION_TASK_EXECUTOR
import cz.ctu.fee.palivtom.transactiongrouperservice.model.EventList
import cz.ctu.fee.palivtom.transactiongrouperservice.model.TableEvent
import cz.ctu.fee.palivtom.transactiongrouperservice.model.Transaction
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.TransactionGroupProducer
import cz.ctu.fee.palivtom.transactiongrouperservice.service.EventTransactionService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Service
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val logger = KotlinLogging.logger {}

@Service
class EventTransactionServiceImpl(
    private val transactionGroupProducer: TransactionGroupProducer,
    @Qualifier(TRANSACTION_TASK_EXECUTOR) private val eventTaskExecutor: TaskExecutor
) : EventTransactionService {

    private val locks = HashMap<String, ReentrantLock>()
    private val transactions = ConcurrentHashMap<String, EventList>()
    private val queue = LinkedList<String>()

    @Synchronized
    private fun getLock(txId: String): ReentrantLock {
        return locks.getOrPut(txId) { ReentrantLock() }
    }

    @Synchronized
    override fun registerTransaction(transaction: Transaction) {
        getLock(transaction.id).withLock {
            logger.info { "Registering transaction: $transaction" }

            if (transactions.containsKey(transaction.id)) {
                transactions[transaction.id]!!.setTransaction(transaction)
            } else {
                transactions[transaction.id] = EventList(transaction)
            }
        }
        queue.add(transaction.id)
        processQueue()
    }

    override fun addEventToTransaction(event: TableEvent) {
        val txId = event.transactionMetadata.txId
        getLock(txId).withLock {
            logger.info { "Adding event: $event" }

            val eventList = transactions.getOrPut(txId) { EventList() }
            val success = eventList.addEvent(event)
            if (!success) {
                return logger.warn { "Duplicated event has occur: $event" }
            }
        }
        processQueue()
    }

    private fun pollEvents(txId: String): EventList {
        val eventList = transactions[txId]!!
        locks.remove(txId)
        transactions.remove(txId)
        return eventList
    }

    @Synchronized
    private fun processQueue() {
        val peekValue = queue.peek() ?: return
        val eventList = transactions[peekValue]
        if (eventList != null && eventList.isComplete()) {
            val pollValue = queue.poll()
            val pollEvents = pollEvents(pollValue)

            eventTaskExecutor.execute {
                transactionGroupProducer.sendTransactionGroup(pollValue, pollEvents)
            }

            processQueue()
        }
    }
}
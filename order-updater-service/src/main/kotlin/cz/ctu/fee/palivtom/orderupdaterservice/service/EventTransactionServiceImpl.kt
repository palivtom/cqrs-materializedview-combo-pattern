package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.orderupdaterservice.producer.TransactionStatusProducer
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
import cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces.EventProcessor
import mu.KotlinLogging
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class EventTransactionServiceImpl(
    private val transactionStatusProducer: TransactionStatusProducer,
    private val eventProcessor: EventProcessor
) : EventTransactionService {
    private val logger = KotlinLogging.logger {}

    private val taskExecutor = SyncTaskExecutor()
    private val transactions = HashMap<String, Pair<Transaction?, MutableList<Event>>>()
    private val transactionsLock = ReentrantLock()

    // TODO what about transaction order?
    // TODO better thread locking - its not necessary to lock whole transaction map

    override fun registerTransaction(transaction: Transaction) {
        transactionsLock.withLock {
            logger.info { "Registering transaction: $transaction" }
            transactionStatusProducer.sendEventTransactionStatus(transaction.id, EventTransactionStatus.RECEIVED)

            val localTransaction = transactions[transaction.id]

            if (localTransaction == null) {
                transactions[transaction.id] = Pair(transaction, mutableListOf())
            } else if (localTransaction.first == null) {
                transactions[transaction.id] = Pair(transaction, localTransaction.second)
                executeEvents(transaction.id)
            } else {
                throw IllegalStateException("Transaction ${transaction.id} already registered.") // todo
            }
        }
    }

    override fun addEventToTransaction(event: Event) {
        transactionsLock.withLock {
            val txId = event.eventMetadata.txId
            logger.info { "Adding event $event to transaction $txId" }
            val transactionPair = transactions[txId]

            if (transactionPair == null) {
                transactions[txId] = Pair(null, mutableListOf(event))
            } else {
                transactionPair.second.add(event)
                if (transactionPair.first != null) {
                    executeEvents(txId)
                }
            }
        }
    }

    private fun executeEvents(txId: String) {
        val transactionPair = transactions[txId]!!
        if (transactionPair.first!!.eventCount != transactionPair.second.size) return

        transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.PROCESSING)

        try {
            taskExecutor.execute {
                proceedEvents(transactionPair.second)
                transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.SUCCESS)
            }
        } catch (e: Exception) {
            logger.error { "Error while processing transaction $txId" }
            transactionStatusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.FAIL)
        }

        transactions.remove(transactionPair.first!!.id)
    }

    @Transactional
    protected fun proceedEvents(events: List<Event>) {
        events.sortedBy { it.eventMetadata.txTotalOrder }
            .forEach { it.accept(eventProcessor) }
    }
}
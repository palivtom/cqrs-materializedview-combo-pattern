package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.DebeziumPsqlWrapperValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.OrderValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.ShippingAddressValue
import cz.ctu.fee.palivtom.orderupdaterservice.producer.TransactionStatusProducer
import mu.KotlinLogging
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventTransactionService(
    private val transactionStatusProducer: TransactionStatusProducer,
    private val orderService: OrderService,
    private val shippingAddressService: ShippingAddressService
) {
    private val logger = KotlinLogging.logger {}

    private val taskExecutor = SyncTaskExecutor()
    private val transactions = HashMap<String, Pair<Transaction?, MutableList<DebeziumPsqlWrapperValue<Any>>>>()

    // todo what about transaction order?

    @Synchronized
    fun registerTransaction(transaction: Transaction) {
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

    @Synchronized
    fun addEventToTransaction(event: DebeziumPsqlWrapperValue<Any>) {
        val txId = event.transaction.id
        logger.info { "Adding event $event to transaction $txId" }
        val transactionPair = transactions[txId]

        if (transactionPair == null) {
            transactions[txId] = Pair(null, mutableListOf(event))
        } else {
            transactionPair.second.add(event)
            executeEvents(txId)
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
    protected fun proceedEvents(events: List<DebeziumPsqlWrapperValue<Any>>) {
        logger.info { "Proceeding transaction: $events" }
        // TODO visitor instead of when and casting

        events.forEach {
            when (it.source.table) {
                "orders" -> {
                    orderService.proceedOrder(it as DebeziumPsqlWrapperValue<OrderValue>)
                }
                "shipping_addresses" -> {
                    shippingAddressService.proceedShippingAddress(it as DebeziumPsqlWrapperValue<ShippingAddressValue>)
                }
                else -> throw IllegalStateException("Unknown table ${it.source.table}")
            }
        }
    }
}
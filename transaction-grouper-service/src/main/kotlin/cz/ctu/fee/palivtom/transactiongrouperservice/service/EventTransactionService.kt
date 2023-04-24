package cz.ctu.fee.palivtom.transactiongrouperservice.service

import cz.ctu.fee.palivtom.transactiongrouperservice.model.TableEvent
import cz.ctu.fee.palivtom.transactiongrouperservice.model.Transaction

interface EventTransactionService {

    /**
     * Registers the transaction.
     *
     * Executes handling if its asynchronously consuming events are already collected.
     */
    fun registerTransaction(transaction: Transaction)

    /**
     * Adds an event to the collection - the collection is used as a grouping per transaction ID.
     *
     * Executes handling if its asynchronously consuming transaction is registered and all events have been collected.
     */
    fun addEventToTransaction(event: TableEvent)
}
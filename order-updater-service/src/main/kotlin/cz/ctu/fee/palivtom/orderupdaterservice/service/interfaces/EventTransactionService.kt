package cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event

interface EventTransactionService {

    /**
     * Registers the transaction.
     *
     * Executes handling if its asynchronous events are already collected.
     *
     * @param transaction transaction details
     */
    fun registerTransaction(transaction: Transaction)

    /**
     * Adds an event to the collection - the collection is used as a grouping per transaction ID.
     *
     * Executes handling if its asynchronous transaction is registered and all events have been collected.
     */
    fun addEventToTransaction(event: Event)
}
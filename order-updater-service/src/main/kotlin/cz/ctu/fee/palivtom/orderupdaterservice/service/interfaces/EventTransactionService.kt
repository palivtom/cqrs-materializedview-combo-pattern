package cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event

interface EventTransactionService {
    fun registerTransaction(transaction: Transaction)
    fun addEventToTransaction(event: Event)
}
package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.UpdateEvent

interface CommandBlocker {
    fun blockUntilViewUpdate(txId: String, opCount: Long, timeout: Long)
    fun unblock(updateEvent: UpdateEvent)
}
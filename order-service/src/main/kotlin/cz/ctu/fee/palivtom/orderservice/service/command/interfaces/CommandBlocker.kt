package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

interface CommandBlocker {
    fun blockUntilViewUpdate(txId: String, timeout: Long)
    fun unblock(txId: String)
}
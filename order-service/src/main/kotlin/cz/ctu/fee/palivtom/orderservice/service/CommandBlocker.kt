package cz.ctu.fee.palivtom.orderservice.service

interface CommandBlocker {
    fun blockUntilViewUpdate(txId: String, timeout: Long)
    fun unblock(txId: String)
}
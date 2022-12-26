package cz.ctu.fee.palivtom.orderservice.service

interface CommandBlocker {
    fun blockWithTimeout(txId: String, timeout: Long)
    fun unblock(txId: String)
}
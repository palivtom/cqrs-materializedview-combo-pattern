package cz.ctu.fee.palivtom.orderservice.service.interfaces

import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException

interface CommandBlocker {
    /**
     * Blocks the current thread for a maximum time of timeout.
     *
     * @param txId transaction ID by which can be released this thread with method [unblock] call.
     * @param timeout time in milliseconds, after a timeout the
     * [CommandBlockerException] is thrown.
     */
    fun blockWithTimeout(txId: String, timeout: Long)

    /**
     * Releases thread that operates with the given transaction ID and
     * throws [CommandBlockerException] if success flag is false.
     *
     * If the transaction ID does not match anything, nothing happens.
     *
     * @param txId transaction ID, identifier of blocked thread
     * @param success true if transaction has been committed
     */
    fun unblock(txId: String, success: Boolean)
}
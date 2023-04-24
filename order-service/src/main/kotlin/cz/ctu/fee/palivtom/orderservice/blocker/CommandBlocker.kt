package cz.ctu.fee.palivtom.orderservice.blocker

import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException

interface CommandBlocker {
    /**
     * Blocks the current thread for a maximum time of timeout.
     *
     * @param txId transaction ID by which can be released this thread with [unblock] call
     * @param timeout time in milliseconds
     *
     * @throws [CommandBlockerException] after the timeout or if [unblock] call has been ended exceptionally
     */
    fun blockWithTimeout(txId: String, timeout: Long)

    /**
     * Releases thread that operates with the given transaction ID.
     *
     * If the transaction ID does not match anything, nothing happens.
     *
     * @param txId transaction ID, identifier of blocked thread
     * @param success true if transaction has been committed
     * @throws [CommandBlockerException] if success flag is false
     */
    fun unblock(txId: String, success: Boolean)
}
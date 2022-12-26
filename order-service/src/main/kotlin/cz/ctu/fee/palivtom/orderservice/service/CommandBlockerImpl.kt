package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.jvm.Throws

@Service
class CommandBlockerImpl : CommandBlocker {

    private val sharedLocks = HashMap<String, SharedLock>()

    @Throws(CommandBlockerException::class)
    override fun blockWithTimeout(txId: String, timeout: Long) {
        val lock = ReentrantLock()
        val condition = lock.newCondition()

        val result = lock.withLock {
            sharedLocks[txId] = SharedLock(lock, condition)
            condition.await(timeout, TimeUnit.MILLISECONDS)
        }

        sharedLocks.remove(txId)

        if (!result) {
            throw CommandBlockerException("Operation with transaction id '$txId' timed out.")
        }
    }

    override fun unblock(txId: String) {
        sharedLocks[txId]?.let {
            it.lock.withLock {
                it.condition.signal()
            }
        }
    }

    private data class SharedLock(
        val lock: ReentrantLock,
        val condition: Condition
    )
}
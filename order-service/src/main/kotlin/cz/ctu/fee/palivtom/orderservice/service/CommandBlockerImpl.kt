package cz.ctu.fee.palivtom.orderservice.service

import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.jvm.Throws

@Service
class CommandBlockerImpl : CommandBlocker {

    private val sharedLocks = mutableMapOf<String, SharedLock>()

    @Throws(Exception::class)
    override fun blockUntilViewUpdate(txId: String, timeout: Long) {
        val lock = ReentrantLock()
        val condition = lock.newCondition()

        val result = lock.withLock {
            sharedLocks[txId] = SharedLock(lock, condition)
            condition.await(timeout, TimeUnit.MILLISECONDS)
        }

        sharedLocks.remove(txId)

        if (!result) {
            // todo implement exception
            throw Exception("Operation with key $txId timed out.")
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
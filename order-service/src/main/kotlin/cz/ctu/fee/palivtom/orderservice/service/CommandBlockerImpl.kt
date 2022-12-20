package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.CommandBlocker
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.UpdateEvent
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
    override fun blockUntilViewUpdate(txId: String, opCount: Long, timeout: Long) {
        val lock = ReentrantLock()
        val condition = lock.newCondition()

        val result = lock.withLock {
            sharedLocks[txId] = SharedLock(opCount, lock, condition)
            condition.await(timeout, TimeUnit.MILLISECONDS)
        }

        sharedLocks.remove(txId)

        if (!result) {
            throw Exception("Operation with key $txId timed out.")
        }
    }

    override fun unblock(updateEvent: UpdateEvent) {
        sharedLocks[updateEvent.transactionId]?.let {
            it.lock.withLock {
                if (--it.opCount == 0L) {
                    it.condition.signal()
                }
            }
        }
    }

    private data class SharedLock(
        var opCount: Long,
        val lock: ReentrantLock,
        val condition: Condition
    )
}
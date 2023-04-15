package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

private val logger = KotlinLogging.logger {}

@Service
class CommandBlockerImpl : CommandBlocker {

    private val completableFutures = ConcurrentHashMap<String, CompletableFuture<Unit>>()

    @Throws(CommandBlockerException::class)
    override fun blockWithTimeout(txId: String, timeout: Long) {
        return CompletableFuture<Unit>()
            .orTimeout(timeout, TimeUnit.MILLISECONDS)
            .let {
                logger.info { "Registering blocking future with transactionId '$txId'." }
                completableFutures[txId] = it
                try {
                    it.join()
                } catch (e: CompletionException) {
                    throw CommandBlockerException(e.message ?: "Command blocker failed.", e)
                } finally {
                    completableFutures.remove(txId)
                }
            }
        }

    override fun unblock(txId: String, success: Boolean) {
        completableFutures[txId]?.apply {
            logger.info { "Completing blocking future with transactionId '$txId'." }
            if (success) {
                complete(Unit)
            } else {
                completeExceptionally(
                    CommandBlockerException("View transaction failed.")
                )
            }
        } ?: logger.warn { "Completing blocking future unsatisfied, transactionId '$txId' not found." }
    }
}
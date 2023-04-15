package cz.ctu.fee.palivtom.orderservice.consumer

import cz.ctu.fee.palivtom.orderservice.service.CommandBlocker
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusKey
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusValue
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * It consumes events that unblock the request thread.
 */
@Component
class TransactionStatusConsumer(
    private val commandBlocker: CommandBlocker
) {
    @KafkaListener(topics = ["\${kafka.topics.event.transaction.status}"], containerFactory = "transactionStatusFactory")
    private fun consumeTransactionStatus(message: ConsumerRecord<TransactionStatusKey, TransactionStatusValue>) {
        logger.debug { "Update event massage $message consumed." }

        try {
            when(message.value().status) {
                TransactionStatusValue.StatusValue.SUCCESS -> commandBlocker.unblock(message.key().txId, true)
                TransactionStatusValue.StatusValue.FAIL -> commandBlocker.unblock(message.key().txId, false)
                else -> logger.debug { "Message $message has been ignored." }
            }
        } catch (e: Exception) {
            logger.error { "Error has occur $e." }
        }
    }
}
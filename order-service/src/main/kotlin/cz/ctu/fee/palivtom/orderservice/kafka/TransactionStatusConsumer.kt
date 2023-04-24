package cz.ctu.fee.palivtom.orderservice.kafka

import cz.ctu.fee.palivtom.orderservice.blocker.CommandBlocker
import cz.ctu.fee.palivtom.updatermodel.kafka.model.UpdaterStatusKey
import cz.ctu.fee.palivtom.updatermodel.kafka.model.UpdaterStatusValue
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
    @KafkaListener(
        topics = ["\${kafka.topics.updater-status}"],
        containerFactory = "transactionStatusFactory"
    )
    private fun consumeTransactionStatus(message: ConsumerRecord<UpdaterStatusKey, UpdaterStatusValue>) {
        logger.debug { "Update event massage $message consumed." }

        when(message.value().status) {
            UpdaterStatusValue.Status.SUCCESS -> commandBlocker.unblock(message.key().txId, true)
            UpdaterStatusValue.Status.FAIL -> commandBlocker.unblock(message.key().txId, false)
            else -> logger.debug { "Message $message has been ignored." }
        }
    }
}
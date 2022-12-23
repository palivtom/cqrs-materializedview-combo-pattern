package cz.ctu.fee.palivtom.orderservice.consumer

import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.CommandBlocker
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusKey
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusValue
import cz.ctu.fee.palivtom.orderviewmodel.utils.OrderKafkaTopics
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionStatusConsumer(
    private val commandBlocker: CommandBlocker
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = [OrderKafkaTopics.EVENT_TRANSACTION_STATUS], containerFactory = "transactionStatusFactory")
    private fun consumeTransactionStatus(message: ConsumerRecord<TransactionStatusKey, TransactionStatusValue>) {
        logger.debug { "Update event massage $message consumed." }
        if (message.value().status == TransactionStatusValue.StatusValue.SUCCESS) {
            commandBlocker.unblock(message.key().txId)
        }
    }
}
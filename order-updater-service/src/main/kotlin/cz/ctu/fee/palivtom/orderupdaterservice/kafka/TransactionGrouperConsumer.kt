package cz.ctu.fee.palivtom.orderupdaterservice.kafka

import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.KafkaMetadataMapper.toMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupKey
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * It consumes database table events and transforms them into application events.
 */
@Component
class TransactionGrouperConsumer(
    private val transactionGrouperHandler: TransactionGrouperHandler
) {
    @KafkaListener(
        topics = ["\${kafka.topics.transaction-grouper}"],
        containerFactory = "transactionGrouperContainerFactory"
    )
    private fun transactionGrouperConsumer(message: ConsumerRecord<TransactionGroupKey, TransactionGroupValue>) {
        logger.debug { "Received message: $message" }

        transactionGrouperHandler.processTransaction(message.value(), message.toMetadata())
    }
}
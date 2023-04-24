package cz.ctu.fee.palivtom.transactiongrouperservice.kafka

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model.TransactionValue
import cz.ctu.fee.palivtom.transactiongrouperservice.model.Transaction
import cz.ctu.fee.palivtom.transactiongrouperservice.service.EventTransactionService
import cz.ctu.fee.palivtom.transactiongrouperservice.utils.mapper.KafkaMapper.toMetadata
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * It consumes database transaction events.
 */
@Component
class TransactionConsumer(
    private val eventTransactionService: EventTransactionService,
) {
    @KafkaListener(
        topics = ["\${kafka.topics.transaction-metadata}"],
        containerFactory = "transactionRecordContainerFactory"
    )
    private fun transactionConsumer(message: ConsumerRecord<JsonNode, TransactionValue>) {
        logger.debug { "Received message: $message" }

        val value = message.value()
        if (value.status == TransactionValue.Status.END && value.eventCount != null && value.eventCount > 0) {
            val collections = value.collectionsToMap()

            eventTransactionService.registerTransaction(
                Transaction(
                    id = value.id,
                    eventCount = value.eventCount,
                    collections = collections,
                    kafkaMetadata = message.toMetadata()
                )
            )
        }
    }
}
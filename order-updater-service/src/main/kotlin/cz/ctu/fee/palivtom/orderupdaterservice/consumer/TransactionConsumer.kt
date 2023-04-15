package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.KafkaMapper.toMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * It consumes database transaction events.
 */
@Component
class TransactionConsumer(
    private val eventTransactionService: EventTransactionService,
    @Value("\${kafka.allowed.collections}") private val allowedCollections: Set<String>
) {
    @KafkaListener(
        topics = ["\${kafka.topics.order-service-db.transactions}"],
        containerFactory = "transactionRecordContainerFactory"
    )
    private fun transactionConsumer(message: ConsumerRecord<JsonNode, TransactionRecordValue>) {
        logger.debug { "Received message: $message" }

        try {
            val value = message.value()

            if (value.status == TransactionRecordValue.Status.END) {
                val collections = value.collectionsToMap()
                val allowedEventCount = calculateAllowedEvents(collections)

                eventTransactionService.registerTransaction(
                    Transaction(
                        id = value.id,
                        eventCount = value.eventCount!!,
                        supportedEventCount = allowedEventCount,
                        collections = collections,
                        kafkaMetadata = message.toMetadata()
                    )
                )
            }
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }

    private fun calculateAllowedEvents(collections: Map<String, Int>): Int = collections
        .filter { entry -> allowedCollections.contains(entry.key) }
        .values
        .sum()
}
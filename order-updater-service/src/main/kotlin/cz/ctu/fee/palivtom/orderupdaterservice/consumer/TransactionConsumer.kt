package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.EventTransactionService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionConsumer(
    private val eventTransactionService: EventTransactionService,
) {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.transactions}"], containerFactory = "transactionRecordContainerFactory")
    private fun transactionConsumer(message: ConsumerRecord<JsonNode, TransactionRecordValue>) {
        logger.debug { "Received message: $message" }

        if (message.value().status == TransactionRecordValue.Status.END && message.value().eventCount!! > 0){
            eventTransactionService.registerTransaction(
                message.value().toEntity()
            )
        }
    }
}
package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.EventTransactionService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TableEventConsumer(
    private val objectMapper: ObjectMapper,
    private val eventTransactionService: EventTransactionService
) {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.orders}"])
    private fun orderConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        val debeziumOrderValue = objectMapper.convertValue(
            message.value(),
            object : TypeReference<DebeziumPsqlWrapperValue<OrderValue>>() {}
        )
        eventTransactionService.addEventToTransaction(
            debeziumOrderValue as DebeziumPsqlWrapperValue<Any>
        )
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.shipping_addresses}"])
    private fun shippingAddressConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        val debeziumShippingAddressValue = objectMapper.convertValue(
            message.value(),
            object : TypeReference<DebeziumPsqlWrapperValue<ShippingAddressValue>>() {}
        )
        eventTransactionService.addEventToTransaction(
            debeziumShippingAddressValue as DebeziumPsqlWrapperValue<Any>
        )
    }
}
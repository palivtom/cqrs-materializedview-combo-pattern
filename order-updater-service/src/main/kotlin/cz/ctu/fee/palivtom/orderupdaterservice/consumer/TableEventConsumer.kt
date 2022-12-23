package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
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

        val value = objectMapper.convertValue(
            message.value(),
            object : TypeReference<DebeziumPsqlWrapperValue<OrderValue>>() {}
        )

        val event = when (value.operation) {
            DebeziumPsqlWrapperValue.Operation.CREATE -> {
                CreateOrderEvent(
                    orderId = value.after!!.id,
                    userId = value.after.userId!!,
                    createdAt = value.after.createdAt!!,
                    eventMetadata =  value.toEventMetadata()
                )
            }

            DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                UpdateOrderEvent(
                    orderId = value.after!!.id,
                    userId = value.after.userId!!,
                    updatedAt = value.after.updatedAt,
                    deletedAt = value.after.deletedAt,
                    eventMetadata = value.toEventMetadata()
                )
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        eventTransactionService.addEventToTransaction(event)
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.shipping_addresses}"])
    private fun shippingAddressConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        val value = objectMapper.convertValue(
            message.value(),
            object : TypeReference<DebeziumPsqlWrapperValue<ShippingAddressValue>>() {}
        )

        val event = when (value.operation) {
            DebeziumPsqlWrapperValue.Operation.CREATE -> {
                CreateShippingAddressEvent(
                    shippingAddressId = value.after!!.id,
                    orderId = value.after.orderId!!,
                    country = value.after.country!!,
                    city = value.after.city!!,
                    zipCode = value.after.zipCode!!,
                    street = value.after.street!!,
                    eventMetadata = value.toEventMetadata()
                )
            }

            DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                UpdateShippingAddressEvent(
                    orderId = value.after!!.orderId!!,
                    country = value.after.country!!,
                    city = value.after.city!!,
                    zipCode = value.after.zipCode!!,
                    street = value.after.street!!,
                    eventMetadata = value.toEventMetadata()
                )
            }

            DebeziumPsqlWrapperValue.Operation.DELETE -> {
                DeleteShippingAddressEvent(
                    shippingAddressId = value.before!!.id,
                    eventMetadata = value.toEventMetadata()
                )
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        eventTransactionService.addEventToTransaction(event)
    }
}
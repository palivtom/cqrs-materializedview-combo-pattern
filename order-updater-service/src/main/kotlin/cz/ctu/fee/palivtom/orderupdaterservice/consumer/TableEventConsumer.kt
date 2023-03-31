package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.EventTransactionService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * It consumes database table events and transforms them into application events.
 */
@Component
class TableEventConsumer(
    private val objectMapper: ObjectMapper,
    private val eventTransactionService: EventTransactionService
) {
    @KafkaListener(topics = ["\${kafka.topics.order-service-db.orders}"])
    private fun orderConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        try {
            val value = objectMapper.convertValue<DebeziumPsqlWrapperValue<OrderValue>>(message.value())

            val event = when (value.operation) {
                DebeziumPsqlWrapperValue.Operation.CREATE -> {
                    CreateOrderEvent(
                        orderId = value.after!!.id,
                        userId = value.after.userId!!,
                        createdAt = value.after.createdAt!!,
                        eventMetadata =  value.getEventMetadata()
                    )
                }

                DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                    UpdateOrderEvent(
                        orderId = value.after!!.id,
                        userId = value.after.userId!!,
                        updatedAt = value.after.updatedAt,
                        deletedAt = value.after.deletedAt,
                        eventMetadata = value.getEventMetadata()
                    )
                }

                else -> return logger.warn { "Unsupported operation: ${value.operation}" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.shipping_addresses}"])
    private fun shippingAddressConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        try {
            val value = objectMapper.convertValue<DebeziumPsqlWrapperValue<ShippingAddressValue>>(message.value())

            val event = when (value.operation) {
                DebeziumPsqlWrapperValue.Operation.CREATE -> {
                    CreateShippingAddressEvent(
                        shippingAddressId = value.after!!.id,
                        orderId = value.after.orderId!!,
                        country = value.after.country!!,
                        city = value.after.city!!,
                        zipCode = value.after.zipCode!!,
                        street = value.after.street!!,
                        eventMetadata = value.getEventMetadata()
                    )
                }

                DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                    UpdateShippingAddressEvent(
                        orderId = value.after!!.orderId!!,
                        country = value.after.country!!,
                        city = value.after.city!!,
                        zipCode = value.after.zipCode!!,
                        street = value.after.street!!,
                        eventMetadata = value.getEventMetadata()
                    )
                }

                DebeziumPsqlWrapperValue.Operation.DELETE -> {
                    DeleteShippingAddressEvent(
                        shippingAddressId = value.before!!.id,
                        eventMetadata = value.getEventMetadata()
                    )
                }

                else -> return logger.warn { "Unsupported operation: ${value.operation}" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }
}
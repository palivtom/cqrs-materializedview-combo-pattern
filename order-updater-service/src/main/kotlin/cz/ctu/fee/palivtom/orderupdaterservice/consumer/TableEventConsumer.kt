package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper
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
                DebeziumPsqlWrapperValue.Operation.CREATE -> CreateOrderEvent(
                    id = value.after!!.id,
                    userId = value.after.userId!!,
                    cartId = value.after.cartId!!,
                    createdAt = value.after.createdAt!!,
                    updatedAt = value.after.updatedAt,
                    deletedAt = value.after.deletedAt,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.UPDATE -> UpdateOrderEvent(
                    id = value.after!!.id,
                    userId = value.after.userId!!,
                    cartId = value.after.cartId!!,
                    createdAt = value.after.createdAt!!,
                    updatedAt = value.after.updatedAt,
                    deletedAt = value.after.deletedAt,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                else -> return logger.warn { "Unsupported operation: $message" }
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
                DebeziumPsqlWrapperValue.Operation.CREATE -> CreateShippingAddressEvent(
                    id = value.after!!.id,
                    orderId = value.after.orderId!!,
                    country = value.after.country!!,
                    city = value.after.city!!,
                    zipCode = value.after.zipCode!!,
                    street = value.after.street!!,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.UPDATE -> UpdateShippingAddressEvent(
                    id = value.after!!.id,
                    orderId = value.after.orderId!!,
                    country = value.after.country!!,
                    city = value.after.city!!,
                    zipCode = value.after.zipCode!!,
                    street = value.after.street!!,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.DELETE -> DeleteShippingAddressEvent(
                    id = value.before!!.id,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                else -> return logger.warn { "Unsupported operation: $message" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.carts}"])
    private fun cartsConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        try {
            val value = objectMapper.convertValue<DebeziumPsqlWrapperValue<CartValue>>(message.value())

            val event = when (value.operation) {
                DebeziumPsqlWrapperValue.Operation.CREATE -> CreateCartEvent(
                    id = value.after!!.id,
                    userId = value.after.userId!!,
                    exportedAt = value.after.exportedAt,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.UPDATE -> UpdateCartEvent(
                    id = value.after!!.id,
                    userId = value.after.userId!!,
                    exportedAt = value.after.exportedAt,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                else -> return logger.warn { "Unsupported operation: $message" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.cart_items}"])
    private fun cartItemsConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        try {
            val value = objectMapper.convertValue<DebeziumPsqlWrapperValue<CartItemValue>>(message.value())

            val event = when (value.operation) {
                DebeziumPsqlWrapperValue.Operation.CREATE -> CreateCartItemEvent(
                    id = value.after!!.id,
                    productNo = value.after.productNo!!,
                    originalPrice = value.after.originalPrice!!,
                    discountPrice = value.after.discountPrice,
                    quantity = value.after.quantity!!,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.UPDATE -> UpdateCartItemEvent(
                    id = value.after!!.id,
                    productNo = value.after.productNo!!,
                    originalPrice = value.after.originalPrice!!,
                    discountPrice = value.after.discountPrice,
                    quantity = value.after.quantity!!,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.DELETE -> DeleteCartItemEvent(
                    id = value.before!!.id,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                else -> return logger.warn { "Unsupported operation: $message" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }

    @KafkaListener(topics = ["\${kafka.topics.order-service-db.cart_items_carts}"])
    private fun cartItemsCartsConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.debug { "Received message: $message" }

        try {
            val value = objectMapper.convertValue<DebeziumPsqlWrapperValue<CartItemCartValue>>(message.value())

            val event = when (value.operation) {
                DebeziumPsqlWrapperValue.Operation.CREATE -> CreateCartItemCartEvent(
                    cartItemId = value.after!!.cartItemId,
                    cartId = value.after.cartId!!,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                DebeziumPsqlWrapperValue.Operation.DELETE -> DeleteCartItemCartEvent(
                    cartItemId = value.before!!.cartItemId,
                    eventMetadata = EventMetadataMapper.toEventMetadata(message, value)
                )

                else -> return logger.warn { "Unsupported operation: $message" }
            }

            eventTransactionService.addEventToTransaction(event)
        } catch (e: Exception) {
            logger.error { "Error has occur $e" }
        }
    }
}
package cz.ctu.fee.palivtom.orderupdaterservice.consumer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.*
import cz.ctu.fee.palivtom.orderupdaterservice.producer.UpdateEventProducer
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Component
class DbUpdateConsumer(
    private val orderViewRepository: OrderViewRepository,
    private val updateEventProducer: UpdateEventProducer,
    private val objectMapper: ObjectMapper,

    @Value("\${kafka.topics.order-service-db.orders}")
    private val ordersTopic: String,
    @Value("\${kafka.topics.order-service-db.shipping_addresses}")
    private val shippingAddressTopic: String
) {

    private val logger = KotlinLogging.logger {}

    // option 1: Single lister for all topics
    @KafkaListener(topics = ["\${kafka.topics.order-service-db.orders}", "\${kafka.topics.order-service-db.shipping_addresses}"])
    private fun genericConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
        logger.info { "Received message opt1: $message" }

        when (message.topic()) {
            ordersTopic -> {
                val orderDto = objectMapper.convertValue(
                    message.value(),
                    object : TypeReference<DebeziumPsqlWrapper<OrderDto>>() {}
                )
                proceedOrder(orderDto)
            }

            shippingAddressTopic -> {
                val shippingAddressDto = objectMapper.convertValue(
                    message.value(),
                    object : TypeReference<DebeziumPsqlWrapper<ShippingAddressDto>>() {}
                )
                proceedShippingAddress(shippingAddressDto)
            }
        }
    }


    //     option 2: Separate listeners for each topic and somehow avoid phantom reads (better say "avoid create entity with same id multiple times")
//    @KafkaListener(topics = ["\${kafka.topics.order-service-db.orders}"])
//    private fun orderConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
//        logger.info { "Received message opt2: $message" }
//
//        val orderDto = objectMapper.convertValue(
//            message.value(),
//            object : TypeReference<DebeziumPsqlWrapper<OrderDto>>() {}
//        )
//        proceedOrder(orderDto)
//    }
//
//    @KafkaListener(topics = ["\${kafka.topics.order-service-db.shipping_addresses}"])
//    private fun shippingAddressConsumer(message: ConsumerRecord<JsonNode, JsonNode>) {
//        logger.info { "Received message opt2: $message" }
//
//        val shippingAddressDto = objectMapper.convertValue(
//            message.value(),
//            object : TypeReference<DebeziumPsqlWrapper<ShippingAddressDto>>() {}
//        )
//        proceedShippingAddress(shippingAddressDto)
//    }

    // TODO move to separate services
    @Transactional(isolation = Isolation.READ_COMMITTED)
    protected fun proceedOrder(value: DebeziumPsqlWrapper<OrderDto>) {

        val toSave = when (value.operation) {
            DebeziumPsqlOperation.CREATE,
            DebeziumPsqlOperation.UPDATE -> {
                val toUpdate = value.after ?: return

                val toUpdateView = orderViewRepository.findById(toUpdate.id).orElse(OrderView(id = toUpdate.id))
                fillOrderAttributes(toUpdateView, toUpdate)
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        orderViewRepository.save(toSave)
        updateEventProducer.fireUpdate(value.source.txId.toString())
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    protected fun proceedShippingAddress(value: DebeziumPsqlWrapper<ShippingAddressDto>) {
        val toSave = when (value.operation) {
            DebeziumPsqlOperation.CREATE,
            DebeziumPsqlOperation.UPDATE -> {
                val toUpdate = value.after ?: return

                val toUpdateView = orderViewRepository.findById(toUpdate.orderId!!).orElse(OrderView(id = toUpdate.orderId!!))
                fillShippingAttributes(toUpdateView, toUpdate)
            }

            DebeziumPsqlOperation.DELETE -> {
                val toDelete = value.before ?: return

                val toUpdateView = orderViewRepository.findByShippingAddressId(toDelete.id) ?: return
                fillShippingAttributes(toUpdateView, ShippingAddressDto())
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        orderViewRepository.save(toSave)
        updateEventProducer.fireUpdate(value.source.txId.toString())
    }

    private fun fillOrderAttributes(toUpdateView: OrderView, toUpdate: OrderDto): OrderView {
        return toUpdateView.apply {
            userId = toUpdate.userId
            createdAt = toUpdate.createdAt
            updatedAt = toUpdate.updatedAt
            deletedAt = toUpdate.deletedAt
        }
    }

    private fun fillShippingAttributes(toUpdateView: OrderView, toUpdate: ShippingAddressDto): OrderView {
        return toUpdateView.apply {
            shippingAddressId = toUpdate.id
            country = toUpdate.country
            city = toUpdate.city
            street = toUpdate.street
            zipCode = toUpdate.zipCode
        }
    }
}
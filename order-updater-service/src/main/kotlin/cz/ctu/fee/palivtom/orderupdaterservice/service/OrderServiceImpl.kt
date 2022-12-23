package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.OrderService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderServiceImpl(
    private val orderViewRepository: OrderViewRepository
) : OrderService {
    private val logger = KotlinLogging.logger {}

    override fun createOrder(event: CreateOrderEvent) {
        logger.info { "Creating order: $event" }

        orderViewRepository.save(
            orderViewRepository
                .findById(event.orderId)
                .orElse(OrderView(
                    id = event.orderId
                )).apply {
                    userId = event.userId
                    createdAt = event.createdAt
                }
        )
    }

    override fun updateOrder(event: UpdateOrderEvent) {
        logger.info { "Updating order: $event" }

        orderViewRepository.save(
            orderViewRepository
                .findById(event.orderId)
                .orElseThrow { RuntimeException("Order with id ${event.orderId} not found.") }
                .apply {
                    userId = event.userId
                    updatedAt = event.updatedAt
                    deletedAt = event.deletedAt
                }
        )
    }
}
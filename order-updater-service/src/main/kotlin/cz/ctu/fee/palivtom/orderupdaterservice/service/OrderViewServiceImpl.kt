package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.CartViewService
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class OrderViewServiceImpl(
    private val orderViewRepository: OrderViewRepository,
    private val cartViewService: CartViewService
) : OrderViewService {
    override fun createOrderView(event: CreateOrderEvent) {
        logger.debug { "Creating order: $event" }

        orderViewRepository.save(
            OrderView(
                id = event.id,
                userId = event.userId,
                cart = cartViewService.getCartView(event.cartId),
                createdAt = event.createdAt,
                updatedAt = event.updatedAt,
                deletedAt = event.deletedAt
            )
        )
    }

    override fun updateOrderView(event: UpdateOrderEvent) {
        logger.debug { "Updating order: $event" }

        orderViewRepository.findById(event.id)
            .orElseThrow { RuntimeException("Order with id '${event.id}' not found.") }
            .apply {
                userId = event.userId
                cart = cartViewService.getCartView(event.cartId)
                updatedAt = event.updatedAt
                deletedAt = event.deletedAt

                orderViewRepository.save(this)
            }
    }
}
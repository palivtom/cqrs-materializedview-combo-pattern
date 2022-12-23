package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.config.db.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.service.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toDto
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toEntity
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderViewMapper.toCommandEntity
import org.springframework.stereotype.Component

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val orderViewService: OrderViewService,
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor
) {

    fun createOrder(toCreate: OrderDto): OrderDto {
        val orderId = orderService.createOrder(toCreate.toEntity())

        commandBlocker.blockUntilViewUpdate(
            hibernateTransactionInterceptor.getTransactionId(),
            3000
        )

        return orderViewService.getOrderView(orderId)
            .toCommandEntity().toDto()
    }

    fun cancelOrder(orderId: Long): OrderDto {
        val orderId = orderService.cancelOrder(orderId)

        commandBlocker.blockUntilViewUpdate(
            hibernateTransactionInterceptor.getTransactionId(),
            3000
        )

        return orderViewService.getOrderView(orderId)
            .toCommandEntity().toDto()
    }

    fun getOrderById(orderId: Long): OrderDto {
        return orderViewService.getOrderView(orderId)
            .toCommandEntity().toDto()
    }

    fun getOrders(): List<OrderDto> {
        return orderViewService.getOrders()
            .map { it.toCommandEntity().toDto() }
    }

    fun updateOrder(orderId: Long, orderDto: OrderDto): OrderDto {
        val orderId = orderService.updateOrder(orderId, orderDto.toEntity())

        commandBlocker.blockUntilViewUpdate(
            hibernateTransactionInterceptor.getTransactionId(),
            3000
        )

        return orderViewService.getOrderView(orderId)
            .toCommandEntity().toDto()
    }
}
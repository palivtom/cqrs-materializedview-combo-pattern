package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.config.db.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.service.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toDto
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toEntity
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderViewMapper.toCommandEntity
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

private const val CREATE_ORDER_TIMEOUT = 3000L
private const val CANCEL_ORDER_TIMEOUT = 3000L
private const val UPDATE_ORDER_TIMEOUT = 3000L

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val orderViewService: OrderViewService,
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor
) {
    fun getOrderById(orderId: Long): OrderDto {
        return orderViewService.getOrderView(orderId)
            .toCommandEntity().toDto()
    }

    fun getOrders(): List<OrderDto> {
        return orderViewService.getOrders()
            .map { it.toCommandEntity().toDto() }
    }

    fun createOrder(toCreate: OrderDto): OrderDto {
        val resultOrderId = orderService.createOrder(toCreate.toEntity())

        propagateResponseOrThrow(CREATE_ORDER_TIMEOUT)

        return orderViewService.getOrderView(resultOrderId)
            .toCommandEntity().toDto()
    }

    fun cancelOrder(orderId: Long): OrderDto {
        val resultOrderId = orderService.cancelOrder(orderId)

        propagateResponseOrThrow(CANCEL_ORDER_TIMEOUT)

        return orderViewService.getOrderView(resultOrderId)
            .toCommandEntity().toDto()
    }

    fun updateOrder(orderId: Long, orderDto: OrderDto): OrderDto {
        val resultOrderId = orderService.updateOrder(orderId, orderDto.toEntity())

        propagateResponseOrThrow(UPDATE_ORDER_TIMEOUT)

        return orderViewService.getOrderView(resultOrderId)
            .toCommandEntity().toDto()
    }

    private fun propagateResponseOrThrow(timeout: Long) {
        try {
            commandBlocker.blockWithTimeout(
                hibernateTransactionInterceptor.getTransactionId(),
                timeout
            )
        } catch (e: CommandBlockerException) {
            logger.error { "Command blocked for too long." }
            throw ApiRuntimeException(e.message!!, HttpStatus.REQUEST_TIMEOUT)
        }
    }
}
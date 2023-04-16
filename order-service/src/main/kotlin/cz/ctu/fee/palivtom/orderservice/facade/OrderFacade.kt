package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.config.db.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.model.OrderViewDto
import cz.ctu.fee.palivtom.orderservice.service.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toEntity
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderViewMapper.toDto
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

private val logger = KotlinLogging.logger {}

private const val CREATE_ORDER_TIMEOUT = 3000L
private const val CANCEL_ORDER_TIMEOUT = 3000L
private const val UPDATE_ORDER_TIMEOUT = 3000L

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val orderViewService: OrderViewService,
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor,
    private val transactionTemplate: TransactionTemplate
) {
    fun getOrderById(orderId: Long): OrderViewDto {
        return transactionTemplate.execute {
            orderViewService.getOrderView(orderId).toDto()
        }!!
    }

    fun getOrders(): List<OrderViewDto> {
        return transactionTemplate.execute {
            orderViewService.getOrders().map { it.toDto() }
        }!!
    }

    fun createOrder(toCreate: OrderDto): OrderViewDto {
        val resultOrderId = transactionTemplate.execute {
            orderService.createOrder(toCreate.toEntity())
        }!!

        propagateResponseOrThrow(CREATE_ORDER_TIMEOUT)

        return transactionTemplate.execute {
            orderViewService.getOrderView(resultOrderId).toDto()
        }!!
    }

    fun cancelOrder(orderId: Long): OrderViewDto {
        transactionTemplate.execute {
            orderService.cancelOrder(orderId)
        }

        propagateResponseOrThrow(CANCEL_ORDER_TIMEOUT)

        return transactionTemplate.execute {
            orderViewService.getOrderView(orderId).toDto()
        }!!
    }

    fun updateOrder(orderId: Long, orderDto: OrderDto): OrderViewDto {
        transactionTemplate.execute {
            orderService.updateOrder(orderId, orderDto.toEntity())
        }

        propagateResponseOrThrow(UPDATE_ORDER_TIMEOUT)

        return transactionTemplate.execute {
            orderViewService.getOrderView(orderId).toDto()
        }!!
    }

    private fun propagateResponseOrThrow(timeout: Long) {
        try {
            commandBlocker.blockWithTimeout(
                hibernateTransactionInterceptor.getTransactionId(),
                timeout
            )
        } catch (e: CommandBlockerException) {
            logger.error(e.message)
            throw ApiRuntimeException(e.message!!, HttpStatus.REQUEST_TIMEOUT)
        }
    }
}
package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.config.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.IdLongDto
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.service.interfaces.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.IdLongMapper.toIdDto
import cz.ctu.fee.palivtom.orderservice.utils.mapper.OrderMapper.toEntity
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
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor,
    private val transactionTemplate: TransactionTemplate
) {
    fun createOrder(toCreate: OrderDto): IdLongDto {
        val resultOrderId = transactionTemplate.execute {
            orderService.createOrder(toCreate.toEntity())
        }!!

        propagateResponseOrThrow(CREATE_ORDER_TIMEOUT)

        return resultOrderId.toIdDto()
    }

    fun cancelOrder(orderId: Long) {
        transactionTemplate.execute {
            orderService.cancelOrder(orderId)
        }

        propagateResponseOrThrow(CANCEL_ORDER_TIMEOUT)
    }

    fun updateOrder(orderId: Long, orderDto: OrderDto) {
        transactionTemplate.execute {
            orderService.updateOrder(orderId, orderDto.toEntity())
        }

        propagateResponseOrThrow(UPDATE_ORDER_TIMEOUT)
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
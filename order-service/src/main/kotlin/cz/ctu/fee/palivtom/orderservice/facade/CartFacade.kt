package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.blocker.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.CartItemDto
import cz.ctu.fee.palivtom.orderservice.blocker.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.CartService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.CartItemMapper.toEntity
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

private const val ADD_TO_CART_TIMEOUT = 10000L
private const val REMOVE_FROM_CART_TIMEOUT = 10000L

@Component
class CartFacade(
    private val cartService: CartService,
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor
) {

    fun addProduct(cartItemDto: CartItemDto) {
        cartService.addToCart(cartItemDto.toEntity())

        propagateResponseOrThrow(ADD_TO_CART_TIMEOUT)
    }

    fun removeProduct(cartItemDto: CartItemDto) {
        cartService.removeFromCart(cartItemDto.toEntity())

        propagateResponseOrThrow(REMOVE_FROM_CART_TIMEOUT)
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
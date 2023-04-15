package cz.ctu.fee.palivtom.orderservice.facade

import cz.ctu.fee.palivtom.orderservice.config.db.HibernateTransactionInterceptor
import cz.ctu.fee.palivtom.orderservice.exceptions.CommandBlockerException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.CartItemDto
import cz.ctu.fee.palivtom.orderservice.model.CartViewDto
import cz.ctu.fee.palivtom.orderservice.service.CommandBlocker
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.CartService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.CartViewService
import cz.ctu.fee.palivtom.orderservice.utils.mapper.CartItemMapper.toEntity
import cz.ctu.fee.palivtom.orderservice.utils.mapper.CartViewMapper.toDto
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

private const val ADD_TO_CART_TIMEOUT = 3000L
private const val REMOVE_FROM_CART_TIMEOUT = 3000L

@Component
class CartFacade(
    private val cartService: CartService,
    private val cartViewService: CartViewService,
    private val commandBlocker: CommandBlocker,
    private val hibernateTransactionInterceptor: HibernateTransactionInterceptor
) {
    fun getCart(): CartViewDto {
        return cartViewService.getCartView().toDto()
    }

    fun addProduct(cartItemDto: CartItemDto): CartViewDto {
        cartService.addToCart(cartItemDto.toEntity())

        propagateResponseOrThrow(ADD_TO_CART_TIMEOUT)

        return cartViewService.getCartView().toDto()
    }

    fun removeProduct(cartItemDto: CartItemDto): CartViewDto {
        cartService.removeFromCart(cartItemDto.toEntity())

        propagateResponseOrThrow(REMOVE_FROM_CART_TIMEOUT)

        return cartViewService.getCartView().toDto()
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
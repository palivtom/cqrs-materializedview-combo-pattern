package cz.ctu.fee.palivtom.orderservice.service.impl

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.MethodNotAllowedApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.Cart
import cz.ctu.fee.palivtom.orderservice.model.CartItem
import cz.ctu.fee.palivtom.orderservice.repository.CartRepository
import cz.ctu.fee.palivtom.orderservice.service.CartItemService
import cz.ctu.fee.palivtom.orderservice.service.CartService
import cz.ctu.fee.palivtom.orderservice.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val userService: UserService,
    private val cartItemService: CartItemService
) : CartService {
    override fun addToCart(toAddCartItem: CartItem) {
        if (toAddCartItem.quantity <= 0) {
            throw MethodNotAllowedApiRuntimeException("Cart item $toAddCartItem has to positive quantity number.")
        }

        val cart = getCart(true)

        cart.cartItems
            .firstOrNull { it.productNo == toAddCartItem.productNo }
            ?.also { cartItemService.updateCartItem(toAddCartItem, it) }
            ?: cartItemService.createCartItem(toAddCartItem, cart)
    }

    override fun removeFromCart(toRemoveCartItem: CartItem) {
        if (toRemoveCartItem.quantity >= 0) {
            throw MethodNotAllowedApiRuntimeException("Cart item $toRemoveCartItem has to negative quantity number.")
        }

        val cart = getCart(true)

        cart.cartItems
            .firstOrNull { it.productNo == toRemoveCartItem.productNo }
            ?.also { cartItemService.updateCartItem(toRemoveCartItem, it) }
            ?: throw MethodNotAllowedApiRuntimeException("Cart item $toRemoveCartItem is not containing it the cart.")
    }

    override fun exportCart(): Cart {
        val cart = getCart()

        if (cart.cartItems.isEmpty()) {
            throw MethodNotAllowedApiRuntimeException("Cart with id '${cart.id} cannot be exported 'cause of its emptiness.'")
        }

        return cartRepository.save(
            cart.apply {
                exportedAt = Instant.now()
            }
        )
    }

    private fun getCart(createIfNotExist: Boolean = false): Cart {
        val userId = userService.getUserId()
        val cart = cartRepository.findByUserIdAndExportedAtNull(userId)

        return if (cart == null && !createIfNotExist) {
            throw NotFoundApiRuntimeException("Current user with id '$userId' does not have a cart.")
        } else {
            cart ?: cartRepository.save(
                Cart(
                    userId = userId
                )
            )
        }
    }
}
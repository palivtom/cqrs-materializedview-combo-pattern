package cz.ctu.fee.palivtom.orderservice.service.command

import cz.ctu.fee.palivtom.orderservice.client.ProductClient
import cz.ctu.fee.palivtom.orderservice.model.command.Cart
import cz.ctu.fee.palivtom.orderservice.model.command.CartItem
import cz.ctu.fee.palivtom.orderservice.repository.command.CartItemRepository
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.CartItemService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartItemServiceImpl(
    private val cartItemRepository: CartItemRepository,
    private val productClient: ProductClient
) : CartItemService {
    override fun createCartItem(toCreateCartItem: CartItem, cart: Cart): CartItem {
        return cartItemRepository.save(
            CartItem(
                productNo = toCreateCartItem.productNo,
                cart = cart,
                originalPrice = productClient.getProductPrice(toCreateCartItem.productNo),
                quantity = toCreateCartItem.quantity
            )
        )
    }

    override fun updateCartItem(toUpdateCartItem: CartItem, cartItem: CartItem) {
        if (toUpdateCartItem.quantity + cartItem.quantity <= 0) {
            cartItemRepository.delete(cartItem)
        } else {
            cartItemRepository.save(
                cartItem.apply {
                    this.quantity += toUpdateCartItem.quantity
                }
            )
        }
    }
}
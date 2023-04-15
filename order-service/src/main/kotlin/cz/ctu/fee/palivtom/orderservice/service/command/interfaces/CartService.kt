package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderservice.model.command.Cart
import cz.ctu.fee.palivtom.orderservice.model.command.CartItem

interface CartService {
    fun addToCart(toAddCartItem: CartItem)
    fun removeFromCart(toRemoveCartItem: CartItem)
    fun exportCart(): Cart
}
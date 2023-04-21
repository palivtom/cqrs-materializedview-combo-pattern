package cz.ctu.fee.palivtom.orderservice.service.interfaces

import cz.ctu.fee.palivtom.orderservice.model.Cart
import cz.ctu.fee.palivtom.orderservice.model.CartItem

interface CartService {
    fun addToCart(toAddCartItem: CartItem)
    fun removeFromCart(toRemoveCartItem: CartItem)
    fun exportCart(): Cart
}
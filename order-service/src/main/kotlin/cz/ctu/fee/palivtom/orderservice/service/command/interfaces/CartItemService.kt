package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderservice.model.command.Cart
import cz.ctu.fee.palivtom.orderservice.model.command.CartItem

interface CartItemService {
    fun createCartItem(toCreateCartItem: CartItem, cart: Cart): CartItem
    fun updateCartItem(toUpdateCartItem: CartItem, cartItem: CartItem)
}
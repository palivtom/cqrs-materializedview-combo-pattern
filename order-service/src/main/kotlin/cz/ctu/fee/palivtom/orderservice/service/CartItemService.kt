package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.model.Cart
import cz.ctu.fee.palivtom.orderservice.model.CartItem

interface CartItemService {
    fun createCartItem(toCreateCartItem: CartItem, cart: Cart): CartItem
    fun updateCartItem(toUpdateCartItem: CartItem, cartItem: CartItem)
}
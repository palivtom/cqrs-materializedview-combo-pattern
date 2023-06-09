package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateCartItemEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteCartItemEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateCartItemEvent
import cz.ctu.fee.palivtom.orderviewmodel.model.CartItemRaw

interface CartItemRawService {
    fun createCartItemRaw(event: CreateCartItemEvent)
    fun updateCartItemRaw(event: UpdateCartItemEvent)
    fun deleteCartItemRaw(event: DeleteCartItemEvent)
    fun getCartItemRaw(id: Long): CartItemRaw
}
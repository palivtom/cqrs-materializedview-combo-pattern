package cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateCartItemCartEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteCartItemEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateCartItemEvent

interface CartItemViewService {
    fun createCartItemView(event: CreateCartItemCartEvent)
    fun updateCartItemView(event: UpdateCartItemEvent)
    fun deleteCartItemView(event: DeleteCartItemEvent)
}
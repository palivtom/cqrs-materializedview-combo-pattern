package cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateCartEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateCartEvent
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView

interface CartViewService {
    fun getCartView(id: Long): CartView
    fun createCartView(event: CreateCartEvent)
    fun updateCartView(event: UpdateCartEvent)
    fun updateCartViewStatistics(id: Long, oldQuantity: Int, newQuantity: Int, oldPrice: Int, newPrice: Int)
}
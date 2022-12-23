package cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent

interface OrderService {
    fun createOrder(event: CreateOrderEvent)
    fun updateOrder(event: UpdateOrderEvent)
}
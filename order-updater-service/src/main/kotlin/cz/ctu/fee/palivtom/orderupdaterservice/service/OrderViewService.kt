package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent

interface OrderViewService {
    fun createOrderView(event: CreateOrderEvent)
    fun updateOrderView(event: UpdateOrderEvent)
}
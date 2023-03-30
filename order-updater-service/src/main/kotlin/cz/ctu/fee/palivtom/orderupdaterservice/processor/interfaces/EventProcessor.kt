package cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateShippingAddressEvent

interface EventProcessor {
    fun process(event: CreateOrderEvent)
    fun process(event: UpdateOrderEvent)
    fun process(event: CreateShippingAddressEvent)
    fun process(event: UpdateShippingAddressEvent)
    fun process(event: DeleteShippingAddressEvent)
}
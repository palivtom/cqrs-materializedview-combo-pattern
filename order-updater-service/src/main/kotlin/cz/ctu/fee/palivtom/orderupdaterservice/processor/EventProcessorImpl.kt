package cz.ctu.fee.palivtom.orderupdaterservice.processor

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.OrderService
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.ShippingAddressService
import cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces.EventProcessor
import org.springframework.stereotype.Component

@Component
class EventProcessorImpl(
    private val orderService: OrderService,
    private val shippingAddressService: ShippingAddressService
) : EventProcessor {
    override fun process(event: CreateOrderEvent) {
        orderService.createOrder(event)
    }

    override fun process(event: UpdateOrderEvent) {
        orderService.updateOrder(event)
    }

    override fun process(event: CreateShippingAddressEvent) {
        shippingAddressService.createShippingAddress(event)
    }

    override fun process(event: UpdateShippingAddressEvent) {
        shippingAddressService.updateShippingAddress(event)
    }

    override fun process(event: DeleteShippingAddressEvent) {
        shippingAddressService.deleteShippingAddress(event)
    }
}
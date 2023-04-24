package cz.ctu.fee.palivtom.orderupdaterservice.processor.impl

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.processor.EventProcessor
import cz.ctu.fee.palivtom.orderupdaterservice.service.*
import org.springframework.stereotype.Component

@Component
class EventProcessorImpl(
    private val orderViewService: OrderViewService,
    private val shippingAddressService: ShippingAddressService,
    private val cartViewService: CartViewService,
    private val cartItemRawService: CartItemRawService,
    private val cartItemViewService: CartItemViewService
) : EventProcessor {
    override fun process(event: CreateOrderEvent) {
        orderViewService.createOrderView(event)
    }

    override fun process(event: UpdateOrderEvent) {
        orderViewService.updateOrderView(event)
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

    override fun process(event: CreateCartEvent) {
        cartViewService.createCartView(event)
    }

    override fun process(event: UpdateCartEvent) {
        cartViewService.updateCartView(event)
    }

    override fun process(event: CreateCartItemEvent) {
        cartItemRawService.createCartItemRaw(event)
    }

    override fun process(event: UpdateCartItemEvent) {
        cartItemRawService.updateCartItemRaw(event)
        cartItemViewService.updateCartItemView(event)
    }

    override fun process(event: DeleteCartItemEvent) {
        cartItemRawService.deleteCartItemRaw(event)
        cartItemViewService.deleteCartItemView(event)
    }

    override fun process(event: CreateCartItemCartEvent) {
        cartItemViewService.createCartItemView(event)
    }

    override fun process(event: DeleteCartItemCartEvent) {
        // nothing to do
    }
}
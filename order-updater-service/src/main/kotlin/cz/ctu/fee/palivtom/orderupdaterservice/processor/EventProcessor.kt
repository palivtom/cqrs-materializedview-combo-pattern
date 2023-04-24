package cz.ctu.fee.palivtom.orderupdaterservice.processor

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*

/**
 * Visitor implementation.
 */
interface EventProcessor {
    fun process(event: CreateOrderEvent)
    fun process(event: UpdateOrderEvent)

    fun process(event: CreateShippingAddressEvent)
    fun process(event: UpdateShippingAddressEvent)
    fun process(event: DeleteShippingAddressEvent)

    fun process(event: CreateCartEvent)
    fun process(event: UpdateCartEvent)

    fun process(event: CreateCartItemEvent)
    fun process(event: UpdateCartItemEvent)
    fun process(event: DeleteCartItemEvent)

    fun process(event: CreateCartItemCartEvent)
    fun process(event: DeleteCartItemCartEvent)
}
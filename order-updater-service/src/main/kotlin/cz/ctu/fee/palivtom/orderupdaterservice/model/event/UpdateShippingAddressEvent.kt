package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces.EventProcessor

data class UpdateShippingAddressEvent(
    val orderId: Long,
    val country: String,
    val city: String,
    val zipCode: String,
    val street: String,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
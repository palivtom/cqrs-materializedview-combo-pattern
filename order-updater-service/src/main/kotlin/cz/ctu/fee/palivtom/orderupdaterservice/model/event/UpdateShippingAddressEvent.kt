package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.model.EventMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.processor.EventProcessor

data class UpdateShippingAddressEvent(
    val id: Long,
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
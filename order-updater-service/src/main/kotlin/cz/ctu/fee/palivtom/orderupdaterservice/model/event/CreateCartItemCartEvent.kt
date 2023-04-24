package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.model.EventMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.processor.EventProcessor

data class CreateCartItemCartEvent(
    val cartItemId: Long,
    val cartId: Long,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
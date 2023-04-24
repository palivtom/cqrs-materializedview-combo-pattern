package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.model.EventMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.processor.EventProcessor

data class UpdateCartItemEvent(
    val id: Long,
    val productNo: String,
    val originalPrice: Int,
    val discountPrice: Int?,
    val quantity: Int,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
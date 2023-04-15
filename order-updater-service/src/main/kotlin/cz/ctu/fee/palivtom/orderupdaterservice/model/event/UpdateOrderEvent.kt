package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces.EventProcessor
import java.time.Instant

data class UpdateOrderEvent(
    val id: Long,
    val userId: String,
    val cartId: Long,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val deletedAt: Instant?,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces.EventProcessor
import java.time.Instant

data class UpdateOrderEvent(
    val orderId: Long,
    val userId: Long,
    val updatedAt: Instant?,
    val deletedAt: Instant?,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
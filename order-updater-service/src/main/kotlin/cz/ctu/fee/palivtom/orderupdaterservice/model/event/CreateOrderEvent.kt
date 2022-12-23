package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces.EventProcessor
import java.time.Instant

data class CreateOrderEvent(
    val orderId: Long,
    val userId: Long,
    val createdAt: Instant,
    override val eventMetadata: EventMetadata
) : Event {
    override fun accept(eventProcessor: EventProcessor) {
        eventProcessor.process(this)
    }
}
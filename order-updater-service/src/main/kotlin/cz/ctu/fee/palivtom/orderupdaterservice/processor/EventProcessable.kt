package cz.ctu.fee.palivtom.orderupdaterservice.processor

/**
 * Visitor implementation.
 */
interface EventProcessable {
    fun accept(eventProcessor: EventProcessor)
}
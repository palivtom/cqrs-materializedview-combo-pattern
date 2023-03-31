package cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces

/**
 * Visitor implementation.
 */
interface EventProcessable {
    fun accept(eventProcessor: EventProcessor)
}
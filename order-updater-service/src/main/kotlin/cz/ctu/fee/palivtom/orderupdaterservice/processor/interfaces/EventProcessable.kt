package cz.ctu.fee.palivtom.orderupdaterservice.processor.interfaces

interface EventProcessable {
    fun accept(eventProcessor: EventProcessor)
}
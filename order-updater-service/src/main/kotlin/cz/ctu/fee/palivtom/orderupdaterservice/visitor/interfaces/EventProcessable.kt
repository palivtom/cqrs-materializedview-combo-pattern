package cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces

interface EventProcessable {
    fun accept(eventProcessor: EventProcessor)
}
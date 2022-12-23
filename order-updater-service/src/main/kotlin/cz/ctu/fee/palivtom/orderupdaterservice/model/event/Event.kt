package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.visitor.interfaces.EventProcessable

interface Event : EventProcessable {
    val eventMetadata: EventMetadata
}
package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue

interface RawEventMapper {

    /**
     * Checks whether the given [rawEvent] is supported by the mapper; if so, it returns true.
     */
    fun supports(rawEvent: TransactionGroupValue.Event): Boolean

    /**
     * Maps [rawEvent] to the application instance of [Event]. If mapping is not supported, [NotImplementedError]
     * is thrown. This situation should not happen if the [supports] method is implemented appropriately.
     */
    fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event
}
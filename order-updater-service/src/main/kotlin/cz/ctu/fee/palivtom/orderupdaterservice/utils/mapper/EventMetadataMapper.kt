package cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper

import cz.ctu.fee.palivtom.orderupdaterservice.model.EventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue

object EventMetadataMapper {
    fun TransactionGroupValue.Event.toEventMetadata(): EventMetadata {
        return EventMetadata(
            txId = txId,
            txTotalOrder = txTotalOrder,
            txCollectionOrder = txCollectionOrder,
            timestamp = timestamp
        )
    }
}
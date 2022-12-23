package cz.ctu.fee.palivtom.orderupdaterservice.model.event

data class EventMetadata(
    val txId: String,
    val txTotalOrder: Int,
    val txCollectionOrder: Int
)
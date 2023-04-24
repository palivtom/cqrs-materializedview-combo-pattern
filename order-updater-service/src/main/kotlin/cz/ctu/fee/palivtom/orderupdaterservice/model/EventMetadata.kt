package cz.ctu.fee.palivtom.orderupdaterservice.model

data class EventMetadata(
    val txId: String,
    val txTotalOrder: Int,
    val txCollectionOrder: Int,
    val timestamp: Long?
)
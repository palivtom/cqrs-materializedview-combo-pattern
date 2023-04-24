package cz.ctu.fee.palivtom.transactiongrouperservice.model

data class TransactionMetadata(
    val txId: String,
    val txTotalOrder: Int,
    val txCollectionOrder: Int
)
package cz.ctu.fee.palivtom.transactiongrouperservice.model

data class Source(
    val version: String,
    val connector: String,
    val name: String,
    val timestamp: Long,
    val snapshot: String?,
    val db: String,
    val schema: String,
    val table: String,
    val txId: Long?
)
package cz.ctu.fee.palivtom.transactiongrouperservice.model

data class TableEvent(
    val operation: String,
    val before: Any?,
    val after: Any?,
    val source: Source,
    val timestamp: Long?,
    val transactionMetadata: TransactionMetadata,
    val kafkaMetadata: KafkaMetadata
)
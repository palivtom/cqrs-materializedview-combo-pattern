package cz.ctu.fee.palivtom.transactiongrouperservice.model

data class KafkaMetadata(
    val partition: Int,
    val offset: Long,
    val timestamp: Long
)
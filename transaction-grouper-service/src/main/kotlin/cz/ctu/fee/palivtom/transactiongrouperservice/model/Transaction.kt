package cz.ctu.fee.palivtom.transactiongrouperservice.model

data class Transaction(
    val id: String,
    val eventCount: Int,
    val collections: Map<String, Int>,
    val kafkaMetadata: KafkaMetadata
)
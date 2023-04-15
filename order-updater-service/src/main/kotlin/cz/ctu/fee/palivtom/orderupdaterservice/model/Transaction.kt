package cz.ctu.fee.palivtom.orderupdaterservice.model

import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.KafkaMetadata

data class Transaction(
    val id: String,
    val eventCount: Int,
    val supportedEventCount: Int,
    val collections: Map<String, Int>,
    val kafkaMetadata: KafkaMetadata
)
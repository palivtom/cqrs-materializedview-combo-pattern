package cz.ctu.fee.palivtom.orderupdaterservice.model.event

import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.KafkaMetadata

data class EventMetadata(
    val txId: String,
    val txTotalOrder: Int,
    val txCollectionOrder: Int,
    val timestamp: Long,
    val kafkaMetadata: KafkaMetadata
)
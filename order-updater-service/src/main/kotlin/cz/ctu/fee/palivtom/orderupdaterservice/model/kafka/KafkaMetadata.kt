package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

data class KafkaMetadata(
    val partition: Int,
    val offset: Long,
    val timestamp: Long
)
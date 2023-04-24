package cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper

import cz.ctu.fee.palivtom.orderupdaterservice.model.KafkaMetadata
import org.apache.kafka.clients.consumer.ConsumerRecord

object KafkaMetadataMapper {
    fun ConsumerRecord<out Any, out Any>.toMetadata() = KafkaMetadata(
        offset = offset(),
        partition = partition(),
        timestamp = timestamp()
    )
}
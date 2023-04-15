package cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper

import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.KafkaMetadata
import org.apache.kafka.clients.consumer.ConsumerRecord

object KafkaMapper {
    fun ConsumerRecord<out Any, out Any>.toMetadata() = KafkaMetadata(
        offset = offset(),
        partition = partition(),
        timestamp = timestamp()
    )
}
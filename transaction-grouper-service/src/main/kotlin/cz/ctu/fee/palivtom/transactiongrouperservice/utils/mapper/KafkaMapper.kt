package cz.ctu.fee.palivtom.transactiongrouperservice.utils.mapper

import cz.ctu.fee.palivtom.transactiongrouperservice.model.KafkaMetadata
import org.apache.kafka.clients.consumer.ConsumerRecord

object KafkaMapper {
    fun ConsumerRecord<out Any, out Any>.toMetadata() = KafkaMetadata(
        offset = offset(),
        partition = partition(),
        timestamp = timestamp()
    )
}
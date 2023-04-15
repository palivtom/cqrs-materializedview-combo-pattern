package cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper

import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.KafkaMapper.toMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.EventMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.DebeziumPsqlWrapperValue
import org.apache.kafka.clients.consumer.ConsumerRecord

object EventMetadataMapper {
    fun toEventMetadata(kafkaMessage: ConsumerRecord<out Any, out Any>, value: DebeziumPsqlWrapperValue<out Any>): EventMetadata {
        return EventMetadata(
            txId = value.transaction!!.id,
            txTotalOrder = value.transaction.totalOrder,
            txCollectionOrder = value.transaction.collectionOrder,
            timestamp = value.timestamp,
            kafkaMetadata = kafkaMessage.toMetadata()
        )
    }
}
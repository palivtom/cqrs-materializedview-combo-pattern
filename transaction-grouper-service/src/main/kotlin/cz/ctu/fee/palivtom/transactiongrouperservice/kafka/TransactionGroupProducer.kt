package cz.ctu.fee.palivtom.transactiongrouperservice.kafka

import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupKey
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import cz.ctu.fee.palivtom.transactiongrouperservice.model.EventList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Produces message of grouped events per transaction ID.
 */
@Component
class TransactionGroupProducer(
    private val kafkaTemplate: KafkaTemplate<TransactionGroupKey, TransactionGroupValue>,
    @Value("\${kafka.topics.output}") private val outputTopic: String
) {
    fun sendTransactionGroup(txId: String, eventList: EventList) {
        logger.debug { "Sending transaction group $eventList" }

        val transaction = eventList.getTransaction()!!
        val events = eventList.getEvents()

        val groupValue = TransactionGroupValue(
            transaction = TransactionGroupValue.Transaction(
                id = transaction.id,
                eventCount = transaction.eventCount,
                collections = transaction.collections.map {
                    TransactionGroupValue.TransactionCollection(
                        name = it.key,
                        count = it.value
                    )
                },
                kafkaMetadata = TransactionGroupValue.KafkaMetadata(
                    partition = transaction.kafkaMetadata.partition,
                    offset = transaction.kafkaMetadata.offset,
                    timestamp = transaction.kafkaMetadata.timestamp
                )
            ),
            events = hashMapOf<String, MutableList<TransactionGroupValue.Event>>().apply {
                events.forEach {
                    val event = TransactionGroupValue.Event(
                        txId = it.transactionMetadata.txId,
                        txTotalOrder = it.transactionMetadata.txTotalOrder,
                        txCollectionOrder = it.transactionMetadata.txCollectionOrder,
                        operation = TransactionGroupValue.Operation.valueOf(it.operation),
                        before = it.before,
                        after = it.after,
                        source = TransactionGroupValue.Source(
                            db = it.source.db,
                            schema = it.source.schema,
                            table = it.source.table,
                            timestamp = it.source.timestamp
                        ),
                        timestamp = it.timestamp,
                        kafkaMetadata = TransactionGroupValue.KafkaMetadata(
                            partition = it.kafkaMetadata.partition,
                            offset = it.kafkaMetadata.offset,
                            timestamp = it.kafkaMetadata.timestamp
                        )
                    )
                    this.getOrPut(event.getCollectionName()) { mutableListOf() }
                        .add(event)
                }
            },
            timestamp = Instant.now().epochSecond
        )

        kafkaTemplate.send(
            outputTopic,
            TransactionGroupKey(txId),
            groupValue
        )
    }
}
package cz.ctu.fee.palivtom.transactiongrouperservice.kafka

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model.DebeziumPsqlTableValue
import cz.ctu.fee.palivtom.transactiongrouperservice.model.Source
import cz.ctu.fee.palivtom.transactiongrouperservice.model.TableEvent
import cz.ctu.fee.palivtom.transactiongrouperservice.model.TransactionMetadata
import cz.ctu.fee.palivtom.transactiongrouperservice.service.EventTransactionService
import cz.ctu.fee.palivtom.transactiongrouperservice.utils.mapper.KafkaMapper.toMetadata
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}
private val allowedOperations = hashSetOf(
    DebeziumPsqlTableValue.Operation.CREATE,
    DebeziumPsqlTableValue.Operation.UPDATE,
    DebeziumPsqlTableValue.Operation.DELETE,
)

/**
 * It consumes database table events.
 */
@Component
class TableConsumer(
    private val eventTransactionService: EventTransactionService
) {
    @KafkaListener(
        topicPattern = "\${kafka.topics.tables-prefix}",
        containerFactory = "tableEventContainerFactory",
        concurrency = "4",
        properties = ["metadata.max.age.ms:5000"]
    )
    private fun tablesConsumer(message: ConsumerRecord<JsonNode, DebeziumPsqlTableValue>) {
        logger.debug { "Received message: $message" }

        val value = message.value()

        if (value.transaction != null && allowedOperations.contains(value.operation)) {
            val tableEvent = TableEvent(
                operation = value.operation.name,
                before = value.before,
                after = value.after,
                source = Source(
                    version = value.source.version,
                    connector = value.source.connector,
                    name = value.source.name,
                    timestamp = value.source.timestamp,
                    snapshot = value.source.snapshot,
                    db = value.source.db,
                    schema = value.source.schema,
                    table = value.source.table,
                    txId = value.source.txId
                ),
                timestamp = value.timestamp,
                transactionMetadata = TransactionMetadata(
                    txId = value.transaction.id,
                    txTotalOrder = value.transaction.totalOrder,
                    txCollectionOrder = value.transaction.collectionOrder
                ),
                kafkaMetadata = message.toMetadata()
            )

            eventTransactionService.addEventToTransaction(tableEvent)
        }
    }
}
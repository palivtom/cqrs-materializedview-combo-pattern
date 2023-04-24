package cz.ctu.fee.palivtom.orderupdaterservice.kafka

import cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper.RawEventMapper
import cz.ctu.fee.palivtom.orderupdaterservice.model.KafkaMetadata
import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.orderupdaterservice.processor.EventProcessor
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class TransactionGrouperHandler(
    private val eventMappers: List<RawEventMapper>,
    private val eventProcessor: EventProcessor,
    private val statusProducer: StatusProducer,
) {
    /**
     * Maps all of supported events and process them in single transaction.
     */
    fun processTransaction(transaction: TransactionGroupValue, kafkaMetadata: KafkaMetadata) {
        val txId = transaction.transaction.id
        logger.info { "Processing transaction with id '$txId': $transaction ($kafkaMetadata)" }

        val events = transaction.events.flatMap { collections ->
            collections.value.mapNotNull { event -> eventMappers
                .firstOrNull { it.supports(event) }
                ?.mapToEvent(event)
                ?: run {
                    logger.warn { "Unsupported event: $event " }
                    null
                }
            }
        }
        processEvents(txId, events)
    }

    @Transactional
    protected fun processEvents(txId: String, events: List<Event>) {
        try {
            statusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.BEGIN)
            events
                .sortedBy { it.eventMetadata.txTotalOrder }
                .forEach { it.accept(eventProcessor) }
            statusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.SUCCESS)
        } catch (e: Exception) {
            logger.error("Error while processing transaction '$txId'.", e)
            statusProducer.sendEventTransactionStatus(txId, EventTransactionStatus.FAIL)
            throw e
        }
    }
}
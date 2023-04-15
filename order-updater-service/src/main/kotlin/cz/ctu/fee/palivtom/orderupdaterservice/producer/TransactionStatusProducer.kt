package cz.ctu.fee.palivtom.orderupdaterservice.producer

import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusKey
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusValue
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Produces state of each consumed and processed transaction.
 */
@Component
class TransactionStatusProducer(
    private val kafkaTemplate: KafkaTemplate<TransactionStatusKey, TransactionStatusValue>,
    @Value("\${kafka.topics.event.transaction.status}") private val eventTransactionStatusTopic: String
) {
    fun sendEventTransactionStatus(ixId: String, status: EventTransactionStatus) {
        logger.debug { "Sending transaction status '$status' for transaction '$ixId'." }

        kafkaTemplate.send(
            eventTransactionStatusTopic,
            TransactionStatusKey(ixId),
            TransactionStatusValue(
                TransactionStatusValue.StatusValue.valueOf(status.name),
                Instant.now()
            )
        )
    }
}
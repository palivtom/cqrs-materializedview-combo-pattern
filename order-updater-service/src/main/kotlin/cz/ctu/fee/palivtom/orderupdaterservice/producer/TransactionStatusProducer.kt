package cz.ctu.fee.palivtom.orderupdaterservice.producer

import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusKey
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.TransactionStatusValue
import cz.ctu.fee.palivtom.orderviewmodel.utils.OrderKafkaTopics.EVENT_TRANSACTION_STATUS
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TransactionStatusProducer(
    private val kafkaTemplate: KafkaTemplate<TransactionStatusKey, TransactionStatusValue>
) {

    private val logger = KotlinLogging.logger {}

    fun sendEventTransactionStatus(ixId: String, status: EventTransactionStatus) {
        logger.debug { "Sending transaction status $status for transaction $ixId" }

        kafkaTemplate.send(
            EVENT_TRANSACTION_STATUS,
            TransactionStatusKey(ixId),
            TransactionStatusValue(
                TransactionStatusValue.StatusValue.valueOf(status.name),
                Instant.now()
            )
        )
    }
}
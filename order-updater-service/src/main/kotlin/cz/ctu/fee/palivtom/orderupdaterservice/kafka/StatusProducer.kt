package cz.ctu.fee.palivtom.orderupdaterservice.kafka

import cz.ctu.fee.palivtom.orderupdaterservice.model.enums.EventTransactionStatus
import cz.ctu.fee.palivtom.updatermodel.kafka.model.UpdaterStatusKey
import cz.ctu.fee.palivtom.updatermodel.kafka.model.UpdaterStatusValue
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
class StatusProducer(
    private val kafkaTemplate: KafkaTemplate<UpdaterStatusKey, UpdaterStatusValue>,
    @Value("\${kafka.topics.updater-status}") private val updaterStatus: String
) {
    fun sendEventTransactionStatus(ixId: String, status: EventTransactionStatus) {
        logger.debug { "Sending transaction status '$status' for transaction '$ixId'." }

        kafkaTemplate.send(
            updaterStatus,
            UpdaterStatusKey(ixId),
            UpdaterStatusValue(
                UpdaterStatusValue.Status.valueOf(status.name),
                Instant.now()
            )
        )
    }
}
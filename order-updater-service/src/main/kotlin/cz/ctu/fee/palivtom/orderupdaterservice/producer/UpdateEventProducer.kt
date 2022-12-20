package cz.ctu.fee.palivtom.orderupdaterservice.producer

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.UpdateEvent
import cz.ctu.fee.palivtom.orderviewmodel.utils.OrderKafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UpdateEventProducer(
    private val kafkaTemplate: KafkaTemplate<JsonNode, UpdateEvent>
) {

    private val logger = mu.KotlinLogging.logger {}

    fun fireUpdate(txId: String) {
        logger.info { "Firing update event for txId: $txId" }
        kafkaTemplate.send(OrderKafkaTopics.UPDATER_EVENT, UpdateEvent(txId))
    }
}
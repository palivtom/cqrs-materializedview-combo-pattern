package cz.ctu.fee.palivtom.orderservice.consumer

import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.CommandBlocker
import cz.ctu.fee.palivtom.orderviewmodel.model.kafka.UpdateEvent
import cz.ctu.fee.palivtom.orderviewmodel.utils.OrderKafkaTopics
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class UpdateEventConsumer(
    private val commandBlocker: CommandBlocker
) {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = [OrderKafkaTopics.UPDATER_EVENT])
    private fun update(message: UpdateEvent) {
        logger.info { "Update event massage $message consumed." }
        commandBlocker.unblock(message)
    }

}
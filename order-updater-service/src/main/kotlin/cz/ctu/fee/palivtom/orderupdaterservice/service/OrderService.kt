package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.DebeziumPsqlWrapperValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.OrderValue
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(
    private val orderViewRepository: OrderViewRepository
) {
    private val logger = KotlinLogging.logger {}

    // todo refactor
    fun proceedOrder(value: DebeziumPsqlWrapperValue<OrderValue>) {
        logger.info { "Proceeding order: $value" }

        val toSave = when (value.operation) {
            DebeziumPsqlWrapperValue.Operation.CREATE,
            DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                val toUpdate = value.after ?: return

                val toUpdateView = orderViewRepository.findById(toUpdate.id).orElse(OrderView(id = toUpdate.id))
                fillOrderAttributes(toUpdateView, toUpdate)
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        orderViewRepository.save(toSave)
    }

    private fun fillOrderAttributes(toUpdateView: OrderView, toUpdate: OrderValue): OrderView {
        return toUpdateView.apply {
            userId = toUpdate.userId
            createdAt = toUpdate.createdAt
            updatedAt = toUpdate.updatedAt
            deletedAt = toUpdate.deletedAt
        }
    }

}
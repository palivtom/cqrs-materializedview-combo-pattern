package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.DebeziumPsqlWrapperValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.ShippingAddressValue
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShippingAddressService(
    private val orderViewRepository: OrderViewRepository
) {

    private val logger = KotlinLogging.logger {}

    // todo refactor
    fun proceedShippingAddress(value: DebeziumPsqlWrapperValue<ShippingAddressValue>) {
        logger.info { "Proceeding shipping address: $value" }

        val toSave = when (value.operation) {
            DebeziumPsqlWrapperValue.Operation.CREATE,
            DebeziumPsqlWrapperValue.Operation.UPDATE -> {
                val toUpdate = value.after ?: return

                val toUpdateView = orderViewRepository.findById(toUpdate.orderId!!).orElse(OrderView(id = toUpdate.orderId!!))
                fillShippingAttributes(toUpdateView, toUpdate)
            }

            DebeziumPsqlWrapperValue.Operation.DELETE -> {
                val toDelete = value.before ?: return

                val toUpdateView = orderViewRepository.findByShippingAddressId(toDelete.id) ?: return
                fillShippingAttributes(toUpdateView, ShippingAddressValue())
            }

            else -> {
                logger.warn { "Unsupported operation: ${value.operation}" }
                return
            }
        }

        orderViewRepository.save(toSave)
    }

    private fun fillShippingAttributes(toUpdateView: OrderView, toUpdate: ShippingAddressValue): OrderView {
        return toUpdateView.apply {
            shippingAddressId = toUpdate.id
            country = toUpdate.country
            city = toUpdate.city
            street = toUpdate.street
            zipCode = toUpdate.zipCode
        }
    }
}
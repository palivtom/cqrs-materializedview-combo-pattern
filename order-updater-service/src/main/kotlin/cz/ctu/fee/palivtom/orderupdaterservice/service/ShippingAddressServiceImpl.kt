package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.ShippingAddressService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShippingAddressServiceImpl(
    private val orderViewRepository: OrderViewRepository
) : ShippingAddressService {

    private val logger = KotlinLogging.logger {}

    override fun createShippingAddress(event: CreateShippingAddressEvent) {
        logger.info { "Creating shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository
                .findById(event.orderId)
                .orElse(OrderView(
                    id = event.orderId
                )).apply {
                    shippingAddressId = event.shippingAddressId
                    country = event.country
                    city = event.city
                    zipCode = event.zipCode
                    street = event.street
                }
        )
    }

    override fun updateShippingAddress(event: UpdateShippingAddressEvent) {
        logger.info { "Updating shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository
                .findById(event.orderId)
                .orElseThrow { RuntimeException("Order with id ${event.orderId} not found.") }
                .apply {
                    country = event.country
                    city = event.city
                    zipCode = event.zipCode
                    street = event.street
                }
        )
    }

    override fun deleteShippingAddress(event: DeleteShippingAddressEvent) {
        logger.info { "Deleting shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository
                .findByShippingAddressId(event.shippingAddressId)
                .orElseThrow { RuntimeException("Order with shipping address id ${event.shippingAddressId} not found.") }
                .apply {
                    shippingAddressId = null
                    country = null
                    city = null
                    zipCode = null
                    street = null
                }
        )
    }
}
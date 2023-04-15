package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.interfaces.ShippingAddressService
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ShippingAddressServiceImpl(
    private val orderViewRepository: OrderViewRepository
) : ShippingAddressService {
    override fun createShippingAddress(event: CreateShippingAddressEvent) {
        logger.debug { "Creating shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository.findById(event.orderId)
                .orElseThrow { RuntimeException("Order with id ${event.orderId} not found.") }
                .apply {
                    shippingAddressId = event.id
                    country = event.country
                    city = event.city
                    zipCode = event.zipCode
                    street = event.street
                }
        )
    }

    override fun updateShippingAddress(event: UpdateShippingAddressEvent) {
        logger.debug { "Updating shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository.findById(event.orderId)
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
        logger.debug { "Deleting shipping address: $event" }

        orderViewRepository.save(
            orderViewRepository.findByShippingAddressId(event.id)
                .orElseThrow { RuntimeException("Order with shipping address id ${event.id} not found.") }
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
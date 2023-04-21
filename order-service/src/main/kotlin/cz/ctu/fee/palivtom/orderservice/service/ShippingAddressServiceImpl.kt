package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.ShippingAddress
import cz.ctu.fee.palivtom.orderservice.repository.OrderRepository
import cz.ctu.fee.palivtom.orderservice.repository.ShippingAddressRepository
import cz.ctu.fee.palivtom.orderservice.service.interfaces.ShippingAddressService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class ShippingAddressServiceImpl(
    private val shippingAddressRepository: ShippingAddressRepository,
    private val orderRepository: OrderRepository
) : ShippingAddressService {
    override fun createShippingAddress(toCreate: ShippingAddress, orderId: Long) {
        val order = orderRepository.getReferenceById(orderId)
        toCreate.order = order

        try {
            shippingAddressRepository.save(toCreate)
        } catch (e: EntityNotFoundException) {
            throw NotFoundApiRuntimeException("Order with id $orderId not found.")
        }
    }

    override fun updateShippingAddress(toUpdate: ShippingAddress?, orderId: Long) {
        val currShippingAddress = shippingAddressRepository.findByOrderId(orderId)

        if (toUpdate != null && currShippingAddress == null) {
            createShippingAddress(toUpdate, orderId)
        } else if (toUpdate != null && currShippingAddress != null) {
            shippingAddressRepository.save(
                currShippingAddress.apply {
                    street = toUpdate.street
                    city = toUpdate.city
                    zipCode = toUpdate.zipCode
                    country = toUpdate.country
                }
            )
        } else if (currShippingAddress != null) {
            shippingAddressRepository.delete(currShippingAddress)
        } else {
            throw NotFoundApiRuntimeException("Shipping address for order with id $orderId not found.")
        }
    }
}
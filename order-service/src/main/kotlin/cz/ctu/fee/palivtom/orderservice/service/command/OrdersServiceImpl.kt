package cz.ctu.fee.palivtom.orderservice.service.command

import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.repository.command.OrderRepository
import cz.ctu.fee.palivtom.orderservice.repository.command.ShippingAddressRepository
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.OrderService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class OrdersServiceImpl(
    private val orderRepository: OrderRepository,
    private val shippingAddressRepository: ShippingAddressRepository
) : OrderService {

    // TODO create shipping address service

    @Transactional
    override fun createOrder(toCreate: Order): Long {
        val shippingAddr = toCreate.shippingAddress // todo redo this

        toCreate.shippingAddress = null
        val order = orderRepository.save(toCreate)

        shippingAddr?.let {
            shippingAddressRepository.save(
                it.apply {
                    this.order = order
                }
            )
        }

        return order.id
    }

    @Transactional
    override fun cancelOrder(orderId: Long): Long {
        val order = orderRepository.save(
            orderRepository.getReferenceById(orderId).apply {
                this.deletedAt = Instant.now()
            }
        )

        return order.id
    }

    @Transactional
    override fun updateOrder(orderId: Long, toUpdate: Order): Long {
        val shippingAddrToUpdate = toUpdate.shippingAddress // todo redo this

        toUpdate.shippingAddress = null
        val order = orderRepository.save(
            orderRepository.getReferenceById(orderId).apply {
                userId = toUpdate.userId
                updatedAt = Instant.now()
            }
        )

        if (shippingAddrToUpdate == null) {
            shippingAddressRepository.delete(
                shippingAddressRepository.getReferenceById(orderId)
            )
        } else {
            shippingAddressRepository.save(
                shippingAddressRepository.findByOrderId(orderId)
                    .orElse(shippingAddrToUpdate.apply {
                        this.order = order
                    }).apply {
                        country = shippingAddrToUpdate.country
                        city = shippingAddrToUpdate.city
                        street = shippingAddrToUpdate.street
                        zipCode = shippingAddrToUpdate.zipCode
                }
            )
        }

        return order.id
    }
}
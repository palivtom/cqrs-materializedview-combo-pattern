package cz.ctu.fee.palivtom.orderservice.service.command

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.repository.command.OrderRepository
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.service.command.interfaces.ShippingAddressService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class OrdersServiceImpl(
    private val shippingAddressService: ShippingAddressService,
    private val orderRepository: OrderRepository
) : OrderService {

    override fun createOrder(toCreate: Order): Long {
        val shippingAddr = toCreate.shippingAddress // owner of this relation is ShippingAddress
        toCreate.shippingAddress = null

        val order = orderRepository.save(toCreate)

        shippingAddr?.let {
            shippingAddressService.createShippingAddress(it, order.id)
        }

        return order.id
    }

    override fun cancelOrder(orderId: Long): Long {
        return try {
            orderRepository.save(
                orderRepository.getReferenceById(orderId).apply {
                    deletedAt = Instant.now()
                }
            ).id
        } catch (e: EntityNotFoundException) {
            throw NotFoundApiRuntimeException("Order with id $orderId not found.")
        }
    }

    override fun updateOrder(orderId: Long, toUpdate: Order): Long {
        shippingAddressService.updateShippingAddress(toUpdate.shippingAddress, orderId)

        return try {
            orderRepository.save(
                orderRepository.getReferenceById(orderId).apply {
                    userId = toUpdate.userId
                    updatedAt = Instant.now()
                }
            ).id
        } catch (e: EntityNotFoundException) {
            throw NotFoundApiRuntimeException("Order with id $orderId not found.")
        }
    }
}
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

    @Transactional
    override fun createOrder(toCreate: Order): Order {
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

        return orderRepository.getReferenceById(order.id)
    }

    @Transactional
    override fun cancelOrder(orderId: Long): Order {
        var order = orderRepository.getReferenceById(orderId)

        order = orderRepository.save(
            order.apply {
                this.removedAt = Instant.now()
            }
        )

        return order
    }

    @Transactional
    override fun updateOrder(orderId: Long, toUpdate: Order): Order {
        var order = orderRepository.getReferenceById(orderId)

        order = orderRepository.save(
            order.apply {
                this.shippingAddress = toUpdate.shippingAddress
                this.updatedAt = Instant.now()
            }
        )

        return order
    }
}
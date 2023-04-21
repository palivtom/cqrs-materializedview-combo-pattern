package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.MethodNotAllowedApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.UnauthorizedApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.Order
import cz.ctu.fee.palivtom.orderservice.repository.OrderRepository
import cz.ctu.fee.palivtom.orderservice.service.interfaces.CartService
import cz.ctu.fee.palivtom.orderservice.service.interfaces.OrderService
import cz.ctu.fee.palivtom.orderservice.service.interfaces.ShippingAddressService
import cz.ctu.fee.palivtom.orderservice.service.interfaces.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class OrderServiceImpl(
    private val userService: UserService,
    private val shippingAddressService: ShippingAddressService,
    private val orderRepository: OrderRepository,
    private val cartService: CartService
) : OrderService {
    override fun createOrder(toCreate: Order): Long {
        val shippingAddr = toCreate.shippingAddress // owner of this relation is ShippingAddress
        toCreate.apply {
            shippingAddress = null
            userId = userService.getUserId()
            cart = cartService.exportCart()
        }

        val order = orderRepository.save(toCreate)

        shippingAddr?.run {
            shippingAddressService.createShippingAddress(this, order.id)
        }

        return order.id
    }

    override fun cancelOrder(orderId: Long) {
        getOrder(orderId)
            .takeIf { it.deletedAt == null }
            ?.apply {
                deletedAt = Instant.now()

                orderRepository.save(this)
            }
            ?: throw MethodNotAllowedApiRuntimeException("Order with id '$orderId' is already canceled.")
    }

    override fun updateOrder(orderId: Long, toUpdate: Order) {
        getOrder(orderId)
            .takeIf { it.deletedAt == null }
            ?.apply {
                updatedAt = Instant.now()

                orderRepository.save(this)
            }
            ?: throw MethodNotAllowedApiRuntimeException("Order with id '$orderId' cannot be updated 'cause order has been canceled.")

        shippingAddressService.updateShippingAddress(toUpdate.shippingAddress, orderId)
    }

    private fun getOrder(orderId: Long) = orderRepository.findById(orderId)
        .orElseThrow { NotFoundApiRuntimeException("Order with id '$orderId' not found.") }
        .takeIf { it.userId == userService.getUserId() }
        ?: throw UnauthorizedApiRuntimeException("Order with id '$orderId' has different owner.")
}
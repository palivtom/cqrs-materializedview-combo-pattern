package cz.ctu.fee.palivtom.orderupdaterservice.service.impl

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateCartEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateCartEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.CartViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.CartViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.CartView
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class CartViewServiceImpl(
    private val cartViewRepository: CartViewRepository
) : CartViewService {
    override fun getCartView(id: Long): CartView {
        return cartViewRepository.findById(id)
            .orElseThrow { RuntimeException("Cart view with id '$id' not found.") }
    }

    override fun createCartView(event: CreateCartEvent) {
        logger.debug { "Creating cart view: $event" }

        cartViewRepository.save(
            CartView(
                id = event.id,
                userId = event.userId,
                exportedAt = event.exportedAt
            )
        )
    }

    override fun updateCartView(event: UpdateCartEvent) {
        logger.debug { "Updating cart view: $event" }

        cartViewRepository.findById(event.id)
            .orElseThrow { RuntimeException("Cart view with id '${event.id}' not found.") }
            .apply {
                userId = event.userId
                exportedAt = event.exportedAt

                cartViewRepository.save(this)
            }
    }

    override fun updateCartViewStatistics(id: Long, oldQuantity: Int, newQuantity: Int, oldPrice: Int, newPrice: Int) {
        logger.debug { "Cart view with id '$id' is updating its statistics." }

        val resultItemQuantity = newQuantity - oldQuantity
        val resultItemPriceSum = oldQuantity * (newPrice - oldPrice) + resultItemQuantity * newPrice

        cartViewRepository.findById(id)
            .orElseThrow { RuntimeException("Cart view with id '$id' not found.") }
            .apply {
                itemCount += resultItemQuantity
                priceSum += resultItemPriceSum
                cartViewRepository.save(this)
            }
    }
}
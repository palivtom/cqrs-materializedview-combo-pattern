package cz.ctu.fee.palivtom.orderupdaterservice.service.impl

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.repository.CartItemRawRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.CartItemRawService
import cz.ctu.fee.palivtom.orderviewmodel.model.CartItemRaw
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class CartItemRawServiceImpl(
    private val cartItemRawRepository: CartItemRawRepository
) : CartItemRawService {
    override fun getCartItemRaw(id: Long): CartItemRaw {
        return cartItemRawRepository.findById(id)
            .orElseThrow{ RuntimeException("Cart item raw with id '$id' not found.") }
    }

    override fun createCartItemRaw(event: CreateCartItemEvent) {
        logger.debug { "Creating cart item raw: $event" }

        cartItemRawRepository.save(
            CartItemRaw(
                id = event.id,
                productNo = event.productNo,
                originalPrice = event.originalPrice,
                discountPrice = event.discountPrice,
                quantity = event.quantity
            )
        )
    }

    override fun updateCartItemRaw(event: UpdateCartItemEvent) {
        logger.debug { "Updating cart item raw: $event" }

        cartItemRawRepository.findById(event.id)
            .orElseThrow { RuntimeException("Cart item raw with id '${event.id}' not found.") }
            .apply {
                originalPrice = event.originalPrice
                discountPrice = event.discountPrice
                quantity = event.quantity

                cartItemRawRepository.save(this)
            }
    }

    override fun deleteCartItemRaw(event: DeleteCartItemEvent) {
        logger.debug { "Removing cart item raw: $event" }

        cartItemRawRepository.deleteById(event.id)
    }
}
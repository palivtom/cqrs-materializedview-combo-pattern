package cz.ctu.fee.palivtom.orderupdaterservice.service.impl

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateCartItemCartEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteCartItemEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateCartItemEvent
import cz.ctu.fee.palivtom.orderupdaterservice.repository.CartItemViewRepository
import cz.ctu.fee.palivtom.orderupdaterservice.service.CartItemRawService
import cz.ctu.fee.palivtom.orderupdaterservice.service.CartItemViewService
import cz.ctu.fee.palivtom.orderupdaterservice.service.CartViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.CartItemView
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class CartItemViewServiceImpl(
    private val cartItemViewRepository: CartItemViewRepository,
    private val cartItemRawService: CartItemRawService,
    private val cartViewService: CartViewService
) : CartItemViewService {
    override fun createCartItemView(event: CreateCartItemCartEvent) {
        logger.debug { "Creating cart item view: $event" }

        val cartItemRaw = cartItemRawService.getCartItemRaw(event.cartItemId)
        cartViewService.updateCartViewStatistics(
            id = event.cartId,
            oldQuantity = 0,
            newQuantity = cartItemRaw.quantity,
            oldPrice = cartItemRaw.discountPrice ?: cartItemRaw.originalPrice,
            newPrice = cartItemRaw.discountPrice ?: cartItemRaw.originalPrice
        )

        cartItemViewRepository.save(
            CartItemView(
                id = cartItemRaw.id,
                productNo = cartItemRaw.productNo,
                cart = cartViewService.getCartView(event.cartId),
                originalPrice = cartItemRaw.originalPrice,
                discountPrice = cartItemRaw.discountPrice,
                quantity = cartItemRaw.quantity
            )
        )
    }

    override fun updateCartItemView(event: UpdateCartItemEvent) {
        logger.debug { "Updating cart item view: $event" }

        cartItemViewRepository.findById(event.id)
            .orElseThrow { RuntimeException("Cart item view with id '${event.id}' not found.") }
            .apply {
                cartViewService.updateCartViewStatistics(
                    id = cart.id,
                    oldQuantity = quantity,
                    newQuantity = event.quantity,
                    oldPrice = discountPrice ?: originalPrice,
                    newPrice = event.discountPrice ?: event.originalPrice
                )

                productNo = event.productNo
                originalPrice = event.originalPrice
                discountPrice = event.discountPrice
                quantity = event.quantity

                cartItemViewRepository.save(this)
            }
    }

    override fun deleteCartItemView(event: DeleteCartItemEvent) {
        logger.debug { "Removing cart item view: $event" }

        cartItemViewRepository.findById(event.id)
            .orElseThrow { RuntimeException("Cart item view with id '${event.id}' not found.") }
            .apply {
                cartViewService.updateCartViewStatistics(
                    id = cart.id,
                    oldQuantity = quantity,
                    newQuantity = 0,
                    oldPrice = discountPrice ?: originalPrice,
                    newPrice = discountPrice ?: originalPrice
                )

                cartItemViewRepository.delete(this)
            }
    }
}
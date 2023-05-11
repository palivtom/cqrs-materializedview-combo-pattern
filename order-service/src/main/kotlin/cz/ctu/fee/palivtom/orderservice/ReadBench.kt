package cz.ctu.fee.palivtom.orderservice

import com.fasterxml.jackson.annotation.JsonProperty
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.repository.CartRepository
import cz.ctu.fee.palivtom.orderservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * TODO: Production not ready, remove this class.
 * This controller is used for benchmark testing only.
 */
@RestController
class ReadBench(
    private val userService: UserService,
    private val cartRepository: CartRepository
) {
    @Transactional
    @GetMapping("/bench-test/cart")
    fun getCartReadBench(): ResponseEntity<CartReadBenchDto> {
        val userId = userService.getUserId()
        val cart = cartRepository.findByUserIdAndExportedAtNull(userId)
            ?: throw NotFoundApiRuntimeException("Cart dost not exist.")

        var priceSum = 0
        var itemCount = 0

        cart.cartItems.forEach {
            priceSum += it.quantity * (it.discountPrice ?: it.originalPrice!!)
            itemCount += it.quantity
        }

        return ResponseEntity.ok(
            CartReadBenchDto(
                userId = cart.userId,
                priceSum = priceSum,
                itemCount = itemCount,
                cartItems = cart.cartItems.map {
                    CartItemReadBenchDto(
                        productNo = it.productNo,
                        quantity = it.quantity,
                        originalPrice = it.originalPrice!!,
                        discountPrice = it.discountPrice
                    )
                }
            )
        )
    }

    data class CartReadBenchDto(
        @JsonProperty("userId") val userId: String,
        @JsonProperty("priceSum") val priceSum: Int,
        @JsonProperty("itemCount") val itemCount: Int,
        @JsonProperty("cartItems") val cartItems: List<CartItemReadBenchDto>
    )

    data class CartItemReadBenchDto(
        @JsonProperty("productNo") val productNo: String,
        @JsonProperty("quantity") val quantity: Int,
        @JsonProperty("originalPrice") val originalPrice: Int,
        @JsonProperty("discountPrice") val discountPrice: Int? = null
    )
}
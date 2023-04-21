package cz.ctu.fee.palivtom.orderservice.api

import cz.ctu.fee.palivtom.orderservice.facade.CartFacade
import cz.ctu.fee.palivtom.orderservice.model.CartItemDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CartController(
    private val cartFacade: CartFacade
) : CartApi {
    override fun addProduct(cartItemDto: CartItemDto): ResponseEntity<Unit> {
        return ResponseEntity(
            cartFacade.addProduct(cartItemDto),
            HttpStatus.NO_CONTENT
        )
    }

    override fun removeProduct(cartItemDto: CartItemDto): ResponseEntity<Unit> {
        return ResponseEntity(
            cartFacade.removeProduct(cartItemDto),
            HttpStatus.NO_CONTENT
        )
    }
}
package cz.ctu.fee.palivtom.orderservice.api

import cz.ctu.fee.palivtom.orderservice.facade.CartFacade
import cz.ctu.fee.palivtom.orderservice.model.CartItemDto
import cz.ctu.fee.palivtom.orderservice.model.CartViewDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CartController(
    private val cartFacade: CartFacade
) : CartApi {
    override fun getCart(): ResponseEntity<CartViewDto> {
        return ResponseEntity(cartFacade.getCart(), HttpStatus.OK)
    }

    override fun addProduct(cartItemDto: CartItemDto): ResponseEntity<CartViewDto> {
        return ResponseEntity(cartFacade.addProduct(cartItemDto), HttpStatus.OK)
    }

    override fun removeProduct(cartItemDto: CartItemDto): ResponseEntity<CartViewDto> {
        return ResponseEntity(cartFacade.removeProduct(cartItemDto), HttpStatus.OK)
    }
}
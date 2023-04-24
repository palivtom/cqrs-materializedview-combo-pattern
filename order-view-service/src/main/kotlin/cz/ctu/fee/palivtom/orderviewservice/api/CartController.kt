package cz.ctu.fee.palivtom.orderviewservice.api

import cz.ctu.fee.palivtom.orderviewservice.model.CartViewDto
import cz.ctu.fee.palivtom.orderviewservice.service.CartViewService
import cz.ctu.fee.palivtom.orderviewservice.utils.mapper.CartViewMapper.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@Transactional
@RestController
class CartController(
    private val cartViewService: CartViewService
) : CartApi {
    override fun getCart(): ResponseEntity<CartViewDto> {
        return ResponseEntity(
            cartViewService.getCartView().toDto(),
            HttpStatus.OK
        )
    }
}
package cz.ctu.fee.palivtom.orderservice.api

import cz.ctu.fee.palivtom.orderservice.facade.OrderFacade
import cz.ctu.fee.palivtom.orderservice.model.IdLongDto
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderFacade: OrderFacade
) : OrdersApi {
    override fun cancelOrder(orderId: Long): ResponseEntity<Unit> {
        return ResponseEntity(
            orderFacade.cancelOrder(orderId),
            HttpStatus.NO_CONTENT
        )
    }

    override fun createOrder(orderDto: OrderDto): ResponseEntity<IdLongDto> {
        return ResponseEntity(
            orderFacade.createOrder(orderDto),
            HttpStatus.CREATED
        )
    }

    override fun updateOrder(orderId: Long, orderDto: OrderDto): ResponseEntity<Unit> {
        return ResponseEntity(
            orderFacade.updateOrder(orderId, orderDto),
            HttpStatus.NO_CONTENT
        )
    }
}
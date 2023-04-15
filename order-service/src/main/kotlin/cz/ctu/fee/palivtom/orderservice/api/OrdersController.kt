package cz.ctu.fee.palivtom.orderservice.api

import cz.ctu.fee.palivtom.orderservice.facade.OrderFacade
import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.model.OrderViewDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class OrdersController(
    private val orderFacade: OrderFacade
) : OrdersApi {
    override fun cancelOrder(orderId: Long): ResponseEntity<OrderViewDto> {
        return ResponseEntity(orderFacade.cancelOrder(orderId), HttpStatus.OK)
    }

    override fun createOrder(orderDto: OrderDto): ResponseEntity<OrderViewDto> {
        return ResponseEntity(orderFacade.createOrder(orderDto), HttpStatus.CREATED)
    }

    override fun getOrderById(orderId: Long): ResponseEntity<OrderViewDto> {
        return ResponseEntity(orderFacade.getOrderById(orderId), HttpStatus.OK)
    }

    override fun getOrders(): ResponseEntity<List<OrderViewDto>> {
        return ResponseEntity(orderFacade.getOrders(), HttpStatus.OK)
    }

    override fun updateOrder(orderId: Long, orderDto: OrderDto): ResponseEntity<OrderViewDto> {
        return ResponseEntity(orderFacade.updateOrder(orderId, orderDto), HttpStatus.OK)
    }
}
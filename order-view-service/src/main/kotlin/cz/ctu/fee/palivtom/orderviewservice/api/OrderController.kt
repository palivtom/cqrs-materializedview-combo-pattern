package cz.ctu.fee.palivtom.orderviewservice.api

import cz.ctu.fee.palivtom.orderviewservice.model.OrderViewDto
import cz.ctu.fee.palivtom.orderviewservice.service.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderviewservice.utils.mapper.OrderViewMapper.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@Transactional
@RestController
class OrderController(
    private val orderViewService: OrderViewService
) : OrdersApi {
    override fun getOrderById(orderId: Long): ResponseEntity<OrderViewDto> {
        return ResponseEntity(
            orderViewService.getOrderView(orderId).toDto(),
            HttpStatus.OK
        )
    }

    override fun getOrders(): ResponseEntity<List<OrderViewDto>> {
        return ResponseEntity(
            orderViewService.getOrdersView().map { it.toDto() },
            HttpStatus.OK
        )
    }
}
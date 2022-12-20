package cz.ctu.fee.palivtom.orderservice.service.querry

import cz.ctu.fee.palivtom.orderservice.repository.query.OrderViewRepository
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import org.springframework.stereotype.Service

@Service
class OrderViewServiceImpl(
    private val orderViewRepository: OrderViewRepository
) : OrderViewService {

    override fun getOrderView(id: Long) = orderViewRepository.findById(id)
        .orElseThrow { RuntimeException("Order with id $id not found") }!!
    override fun getOrders(): List<OrderView> = orderViewRepository.findAll()
}
package cz.ctu.fee.palivtom.orderservice.service.querry

import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.repository.query.OrderViewRepository
import cz.ctu.fee.palivtom.orderservice.service.UserService
import cz.ctu.fee.palivtom.orderservice.service.querry.interfaces.OrderViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView
import org.springframework.stereotype.Service

@Service
class OrderViewServiceImpl(
    private val userService: UserService,
    private val orderViewRepository: OrderViewRepository
) : OrderViewService {

    override fun getOrderView(id: Long) = orderViewRepository
        .findByIdAndUserId(id, userService.getUserId())
        ?: throw NotFoundApiRuntimeException("Order with id '$id' not found.")
    override fun getOrders(): List<OrderView> = orderViewRepository
        .findAllByUserId(userService.getUserId())
}
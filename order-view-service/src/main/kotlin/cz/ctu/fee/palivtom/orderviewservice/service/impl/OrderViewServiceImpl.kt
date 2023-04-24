package cz.ctu.fee.palivtom.orderviewservice.service.impl

import cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime.NotFoundApiRuntimeException
import cz.ctu.fee.palivtom.orderviewservice.repository.OrderViewRepository
import cz.ctu.fee.palivtom.orderviewservice.service.OrderViewService
import cz.ctu.fee.palivtom.orderviewmodel.model.OrderView
import cz.ctu.fee.palivtom.orderviewservice.service.UserService
import org.springframework.stereotype.Service

@Service
class OrderViewServiceImpl(
    private val userService: UserService,
    private val orderViewRepository: OrderViewRepository
) : OrderViewService {
    override fun getOrderView(id: Long) = orderViewRepository
        .findByIdAndUserId(id, userService.getUserId())
        ?: throw NotFoundApiRuntimeException("Order with id '$id' not found.")
    override fun getOrdersView(): List<OrderView> = orderViewRepository
        .findAllByUserId(userService.getUserId())
}
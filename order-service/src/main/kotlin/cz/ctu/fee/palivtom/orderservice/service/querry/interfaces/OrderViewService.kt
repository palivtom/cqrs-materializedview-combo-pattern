package cz.ctu.fee.palivtom.orderservice.service.querry.interfaces

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView

interface OrderViewService {
    fun getOrderView(id: Long): OrderView
    fun getOrders(): List<OrderView>
}
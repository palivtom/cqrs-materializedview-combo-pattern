package cz.ctu.fee.palivtom.orderviewservice.service.interfaces

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView

interface OrderViewService {
    fun getOrderView(id: Long): OrderView
    fun getOrdersView(): List<OrderView>
}
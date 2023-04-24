package cz.ctu.fee.palivtom.orderviewservice.service

import cz.ctu.fee.palivtom.orderviewmodel.model.OrderView

interface OrderViewService {
    fun getOrderView(id: Long): OrderView
    fun getOrdersView(): List<OrderView>
}
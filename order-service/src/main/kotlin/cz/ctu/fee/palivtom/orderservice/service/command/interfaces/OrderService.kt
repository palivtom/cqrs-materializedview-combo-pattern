package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderservice.model.command.Order

interface OrderService {
    fun createOrder(toCreate: Order): Order
    fun cancelOrder(orderId: Long): Order
    fun updateOrder(orderId: Long, toUpdate: Order): Order
}
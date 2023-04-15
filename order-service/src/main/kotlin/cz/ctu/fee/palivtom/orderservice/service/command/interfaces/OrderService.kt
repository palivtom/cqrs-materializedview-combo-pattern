package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderservice.model.command.Order

interface OrderService {
    fun createOrder(toCreate: Order): Long
    fun cancelOrder(orderId: Long)
    fun updateOrder(orderId: Long, toUpdate: Order)
}
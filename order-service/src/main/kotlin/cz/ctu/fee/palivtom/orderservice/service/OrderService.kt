package cz.ctu.fee.palivtom.orderservice.service

import cz.ctu.fee.palivtom.orderservice.model.Order

interface OrderService {
    fun createOrder(toCreate: Order): Long
    fun cancelOrder(orderId: Long)
    fun updateOrder(orderId: Long, toUpdate: Order)
}
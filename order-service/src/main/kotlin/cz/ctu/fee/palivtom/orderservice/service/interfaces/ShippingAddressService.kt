package cz.ctu.fee.palivtom.orderservice.service.interfaces

import cz.ctu.fee.palivtom.orderservice.model.ShippingAddress

interface ShippingAddressService {
    fun createShippingAddress(toCreate: ShippingAddress, orderId: Long)
    fun updateShippingAddress(toUpdate: ShippingAddress?, orderId: Long)
}
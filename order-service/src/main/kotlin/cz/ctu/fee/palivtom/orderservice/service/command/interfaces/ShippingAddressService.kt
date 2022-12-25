package cz.ctu.fee.palivtom.orderservice.service.command.interfaces

import cz.ctu.fee.palivtom.orderservice.model.command.ShippingAddress

interface ShippingAddressService {

    fun createShippingAddress(toCreate: ShippingAddress, orderId: Long)
    fun updateShippingAddress(toUpdate: ShippingAddress?, orderId: Long)
}
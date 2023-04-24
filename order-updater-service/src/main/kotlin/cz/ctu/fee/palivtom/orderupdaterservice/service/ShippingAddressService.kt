package cz.ctu.fee.palivtom.orderupdaterservice.service

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.DeleteShippingAddressEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateShippingAddressEvent

interface ShippingAddressService {
    fun createShippingAddress(event: CreateShippingAddressEvent)
    fun updateShippingAddress(event: UpdateShippingAddressEvent)
    fun deleteShippingAddress(event: DeleteShippingAddressEvent)
}
package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.model.command.ShippingAddress
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView

object OrderViewMapper {
    fun OrderView.toCommandEntity() = Order(
        id = id,
        userId = userId!!,
        createdAt = createdAt!!,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        shippingAddress = country?.let {
            ShippingAddress(
                country = country!!,
                city = city!!,
                street = street!!,
                zipCode = zipCode!!
            )
        }
    )
}
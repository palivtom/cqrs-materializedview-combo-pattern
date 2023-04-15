package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.OrderViewDto
import cz.ctu.fee.palivtom.orderservice.model.ShippingAddressDto
import cz.ctu.fee.palivtom.orderservice.utils.mapper.CartViewMapper.toDto
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.OrderView

object OrderViewMapper {
    fun OrderView.toDto() = OrderViewDto(
        id = id,
        userId = userId,
        cart = cart.toDto(),
        createdAt = createdAt.epochSecond,
        updatedAt = updatedAt?.epochSecond,
        deletedAt = deletedAt?.epochSecond,
        shippingAddress = country?.let {
            ShippingAddressDto(
                country = country!!,
                city = city!!,
                street = street!!,
                zipCode = zipCode!!
            )
        }
    )
}
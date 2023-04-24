package cz.ctu.fee.palivtom.orderviewservice.utils.mapper

import cz.ctu.fee.palivtom.orderviewmodel.model.OrderView
import cz.ctu.fee.palivtom.orderviewservice.model.OrderViewDto
import cz.ctu.fee.palivtom.orderviewservice.model.ShippingAddressViewDto
import cz.ctu.fee.palivtom.orderviewservice.utils.mapper.CartViewMapper.toDto

object OrderViewMapper {
    fun OrderView.toDto() = OrderViewDto(
        id = id,
        userId = userId,
        cart = cart.toDto(),
        createdAt = createdAt.epochSecond,
        updatedAt = updatedAt?.epochSecond,
        deletedAt = deletedAt?.epochSecond,
        shippingAddress = country?.let {
            ShippingAddressViewDto(
                country = country!!,
                city = city!!,
                street = street!!,
                zipCode = zipCode!!
            )
        }
    )
}
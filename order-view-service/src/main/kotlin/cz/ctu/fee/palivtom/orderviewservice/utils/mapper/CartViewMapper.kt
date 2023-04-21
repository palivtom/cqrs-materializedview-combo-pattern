package cz.ctu.fee.palivtom.orderviewservice.utils.mapper

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView
import cz.ctu.fee.palivtom.orderviewservice.model.CartViewDto
import cz.ctu.fee.palivtom.orderviewservice.utils.mapper.CartItemViewMapper.toDto

object CartViewMapper {
    fun CartView.toDto() = CartViewDto(
        userId = userId,
        cartItems = cartItems.map { it.toDto() },
        priceSum = priceSum,
        itemCount = itemCount
    )
}
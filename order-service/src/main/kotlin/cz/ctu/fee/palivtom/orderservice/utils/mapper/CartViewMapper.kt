package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.CartViewDto
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartView

object CartViewMapper {
    fun CartView.toDto() = CartViewDto(
        userId = userId,
        priceSum = priceSum,
        itemCount = itemCount
    )
}
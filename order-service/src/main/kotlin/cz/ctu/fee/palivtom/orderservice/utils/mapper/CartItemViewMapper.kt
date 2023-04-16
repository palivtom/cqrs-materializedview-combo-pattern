package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.CartItemViewDto
import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartItemView

object CartItemViewMapper {
    fun CartItemView.toDto() = CartItemViewDto(
        productNo = productNo,
        quantity = quantity,
        originalPrice = originalPrice,
        discountPrice = discountPrice
    )
}
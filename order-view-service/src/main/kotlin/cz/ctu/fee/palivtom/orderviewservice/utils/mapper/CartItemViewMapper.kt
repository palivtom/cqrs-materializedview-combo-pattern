package cz.ctu.fee.palivtom.orderviewservice.utils.mapper

import cz.ctu.fee.palivtom.orderviewmodel.model.entity.CartItemView
import cz.ctu.fee.palivtom.orderviewservice.model.CartItemViewDto

object CartItemViewMapper {
    fun CartItemView.toDto() = CartItemViewDto(
        productNo = productNo,
        quantity = quantity,
        originalPrice = originalPrice,
        discountPrice = discountPrice
    )
}
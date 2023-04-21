package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.CartItemDto
import cz.ctu.fee.palivtom.orderservice.model.CartItem

object CartItemMapper {
    fun CartItemDto.toEntity() = CartItem(
        productNo = productNo,
        quantity = quantity
    )
}
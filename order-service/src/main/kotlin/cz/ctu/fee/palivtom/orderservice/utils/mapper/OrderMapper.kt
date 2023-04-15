package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.utils.mapper.ShippingAddressMapper.toEntity

object OrderMapper {
    fun OrderDto.toEntity() = Order(
        shippingAddress = shippingAddress?.toEntity()
    )
}
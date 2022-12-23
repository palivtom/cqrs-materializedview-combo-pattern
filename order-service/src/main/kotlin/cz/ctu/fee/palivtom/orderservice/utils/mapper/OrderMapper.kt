package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.OrderDto
import cz.ctu.fee.palivtom.orderservice.model.command.Order
import cz.ctu.fee.palivtom.orderservice.utils.mapper.ShippingAddressMapper.toDto
import cz.ctu.fee.palivtom.orderservice.utils.mapper.ShippingAddressMapper.toEntity

object OrderMapper {

    fun OrderDto.toEntity() = Order(
        userId = userId!!,
        shippingAddress = shippingAddress?.toEntity()
    )

    fun Order.toDto() = OrderDto(
        id = id,
        userId = userId,
        createdAt = createdAt.epochSecond,
        updatedAt = updatedAt?.epochSecond,
        deletedAt = deletedAt?.epochSecond,
        shippingAddress = shippingAddress?.toDto()
    )

}
package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.ShippingAddressDto
import cz.ctu.fee.palivtom.orderservice.model.command.ShippingAddress

object ShippingAddressMapper {
    fun ShippingAddressDto.toEntity() = ShippingAddress(
        street = street,
        city = city,
        zipCode = zipCode,
        country = country
    )

    fun ShippingAddress.toDto() = ShippingAddressDto(
        street = street,
        city = city,
        zipCode = zipCode,
        country = country
    )
}
package cz.ctu.fee.palivtom.orderservice.utils.mapper

import cz.ctu.fee.palivtom.orderservice.model.ShippingAddress
import cz.ctu.fee.palivtom.orderservice.model.ShippingAddressDto

object ShippingAddressMapper {
    fun ShippingAddressDto.toEntity() = ShippingAddress(
        street = street,
        city = city,
        zipCode = zipCode,
        country = country
    )
}
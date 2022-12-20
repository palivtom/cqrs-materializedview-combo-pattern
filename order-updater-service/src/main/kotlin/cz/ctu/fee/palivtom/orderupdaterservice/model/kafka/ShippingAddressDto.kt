package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class ShippingAddressDto(
    @JsonProperty("id")
    var id: Long = 0,

    @JsonProperty("country")
    var country: String? = null,

    @JsonProperty("city")
    var city: String? = null,

    @JsonProperty("street")
    var street: String? = null,

    @JsonProperty("zip_code")
    var zipCode: String? = null,

    @JsonProperty("order_id")
    var orderId: Long? = null
)

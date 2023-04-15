package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class CartItemValue(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("product_no")
    val productNo: String?,

    @JsonProperty("original_price")
    val originalPrice: Int?,

    @JsonProperty("discount_price")
    val discountPrice: Int?,

    @JsonProperty("quantity")
    val quantity: Int?
)
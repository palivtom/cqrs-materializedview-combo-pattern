package cz.ctu.fee.palivtom.orderupdaterservice.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CartItemCartValue(
    @JsonProperty("cart_item_id")
    val cartItemId: Long,

    @JsonProperty("cart_id")
    val cartId: Long?
)
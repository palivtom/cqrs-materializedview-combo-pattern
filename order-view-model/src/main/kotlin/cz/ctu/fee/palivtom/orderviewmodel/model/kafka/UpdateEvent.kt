package cz.ctu.fee.palivtom.orderviewmodel.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateEvent(
    @JsonProperty("transactionId")
    val transactionId: String
)
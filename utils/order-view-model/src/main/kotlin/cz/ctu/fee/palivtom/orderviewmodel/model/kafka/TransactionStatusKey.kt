package cz.ctu.fee.palivtom.orderviewmodel.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionStatusKey(
    @JsonProperty("txId")
    val txId: String
)
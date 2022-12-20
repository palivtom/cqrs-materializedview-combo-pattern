package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class DebeziumPsqlSource(
    @JsonProperty("txId")
    val txId: Long
)
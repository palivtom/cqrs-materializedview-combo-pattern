package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class DebeziumPsqlWrapper<T>(
    @JsonProperty("op")
    val operation: DebeziumPsqlOperation,

    @JsonProperty("before")
    val before: T? = null,

    @JsonProperty("after")
    val after: T? = null,

    @JsonProperty("source")
    val source: DebeziumPsqlSource,

    @JsonProperty("ts_ms")
    val timestamp: Long
)

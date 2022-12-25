package cz.ctu.fee.palivtom.orderviewmodel.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class TransactionStatusValue(
    @JsonProperty("status")
    val status: StatusValue,

    @JsonProperty("timestamp")
    val timestamp: Instant
) {
    enum class StatusValue {
        BEGIN,
        PROCESSING,
        FAIL,
        SUCCESS
    }
}
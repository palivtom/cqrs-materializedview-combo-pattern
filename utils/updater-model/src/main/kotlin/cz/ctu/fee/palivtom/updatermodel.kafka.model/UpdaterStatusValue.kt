package cz.ctu.fee.palivtom.updatermodel.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class UpdaterStatusValue(
    @JsonProperty("status", required = true)
    val status: Status,

    @JsonProperty("timestamp", required = true)
    val timestamp: Instant
) {
    enum class Status {
        BEGIN,
        FAIL,
        SUCCESS
    }
}
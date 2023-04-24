package cz.ctu.fee.palivtom.updatermodel.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdaterStatusKey(
    @JsonProperty("txId", required = true)
    val txId: String
)
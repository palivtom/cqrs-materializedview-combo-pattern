package cz.ctu.fee.palivtom.orderupdaterservice.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.ctu.fee.palivtom.orderupdaterservice.jackson.InstantPsqlDeserializer
import java.time.Instant

data class CartValue(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("user_id")
    val userId: String?,

    @JsonProperty("exported_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    val exportedAt: Instant?
)
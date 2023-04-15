package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.ctu.fee.palivtom.orderupdaterservice.deserializer.InstantPsqlDeserializer
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
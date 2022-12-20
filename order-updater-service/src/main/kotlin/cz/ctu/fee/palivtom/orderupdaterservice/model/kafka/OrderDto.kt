package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.ctu.fee.palivtom.orderupdaterservice.deserializer.InstantPsqlDeserializer
import java.time.Instant

data class OrderDto(
    @JsonProperty("id")
    var id: Long,

    @JsonProperty("user_id")
    var userId: Long? = null,

    @JsonProperty("created_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    var createdAt: Instant? = null,

    @JsonProperty("updated_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    var updatedAt: Instant? = null,

    @JsonProperty("deleted_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    var deletedAt: Instant? = null,
)
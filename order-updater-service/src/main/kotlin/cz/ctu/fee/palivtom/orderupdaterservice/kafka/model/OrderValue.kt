package cz.ctu.fee.palivtom.orderupdaterservice.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.ctu.fee.palivtom.orderupdaterservice.jackson.InstantPsqlDeserializer
import java.time.Instant

data class OrderValue(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("user_id")
    val userId: String?,

    @JsonProperty("cart_id")
    val cartId: Long?,

    @JsonProperty("created_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    val createdAt: Instant?,

    @JsonProperty("updated_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    val updatedAt: Instant?,

    @JsonProperty("deleted_at")
    @JsonDeserialize(using = InstantPsqlDeserializer::class)
    val deletedAt: Instant?
)
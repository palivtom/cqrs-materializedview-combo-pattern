package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.Transaction

data class TransactionRecordValue(
    @JsonProperty("status")
    val status: Status,

    @JsonProperty("id")
    val id: String,

    @JsonProperty("event_count")
    val eventCount: Int? = null,

    @JsonProperty("data_collections")
    val dataCollections: List<TransactionCollection>? = null
) {
    enum class Status(
        private val value: String
    ) {

        BEGIN("BEGIN"),
        END("END");

        @JsonValue
        fun getValue(): String {
            return value
        }
    }

    data class TransactionCollection(
        @JsonProperty("event_count")
        val eventCount: Int,

        @JsonProperty("data_collection")
        val dataCollection: String
    )

    fun toEntity() = Transaction(
        id = id,
        eventCount = eventCount!!
    )
}
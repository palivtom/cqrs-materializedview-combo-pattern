package cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionValue(
    @JsonProperty("status")
    val status: Status,

    @JsonProperty("id")
    val id: String,

    @JsonProperty("event_count")
    val eventCount: Int? = null,

    @JsonProperty("data_collections")
    val dataCollections: List<TransactionCollection>? = null
) {
    enum class Status {
        @JsonProperty("BEGIN")
        BEGIN,

        @JsonProperty("END")
        END;
    }

    data class TransactionCollection(
        @JsonProperty("event_count")
        val eventCount: Int,

        @JsonProperty("data_collection")
        val dataCollection: String
    )

    fun collectionsToMap(): Map<String, Int> = dataCollections
        ?.associateBy({it.dataCollection}, {it.eventCount})
        ?: emptyMap()
}
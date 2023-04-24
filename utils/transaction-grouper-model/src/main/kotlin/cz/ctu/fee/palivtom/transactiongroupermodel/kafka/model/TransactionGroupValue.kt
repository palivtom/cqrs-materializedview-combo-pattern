package cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionGroupValue(
    @JsonProperty("transaction", required = true)
    val transaction: Transaction,

    @JsonProperty("events", required = true)
    val events: Map<String, List<Event>>,

    @JsonProperty("timestamp", required = true)
    val timestamp: Long
) {
    data class Event(
        @JsonProperty("txId", required = true)
        val txId: String,

        @JsonProperty("txTotalOrder", required = true)
        val txTotalOrder: Int,

        @JsonProperty("txCollectionOrder", required = true)
        val txCollectionOrder: Int,

        @JsonProperty("operation", required = true)
        val operation: Operation,

        @JsonProperty("before")
        val before: Any? = null,

        @JsonProperty("after")
        val after: Any? = null,

        @JsonProperty("source", required = true)
        val source: Source,

        @JsonProperty("timestamp")
        val timestamp: Long?,

        @JsonProperty("kafkaMetadata", required = true)
        val kafkaMetadata: KafkaMetadata
    ) {
        fun getCollectionName(): String = "${source.schema}.${source.table}"
    }

    enum class Operation {
        @JsonProperty("c")
        CREATE,

        @JsonProperty("u")
        UPDATE,

        @JsonProperty("d")
        DELETE;
    }

    data class Transaction(
        @JsonProperty("id", required = true)
        val id: String,

        @JsonProperty("eventCount", required = true)
        val eventCount: Int,

        @JsonProperty("collections", required = true)
        val collections: List<TransactionCollection>,

        @JsonProperty("kafkaMetadata", required = true)
        val kafkaMetadata: KafkaMetadata
    )

    data class TransactionCollection(
        @JsonProperty("name", required = true)
        val name: String,

        @JsonProperty("count", required = true)
        val count: Int
    )

    data class Source(
        @JsonProperty("db", required = true)
        val db: String,

        @JsonProperty("schema", required = true)
        val schema: String,

        @JsonProperty("table", required = true)
        val table: String,

        @JsonProperty("timestamp", required = true)
        val timestamp: Long
    )

    data class KafkaMetadata(
        @JsonProperty("partition", required = true)
        val partition: Int,

        @JsonProperty("offset", required = true)
        val offset: Long,

        @JsonProperty("timestamp", required = true)
        val timestamp: Long
    )
}
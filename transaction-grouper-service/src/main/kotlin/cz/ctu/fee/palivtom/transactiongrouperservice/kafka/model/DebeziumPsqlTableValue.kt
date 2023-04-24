package cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class DebeziumPsqlTableValue(
    @JsonProperty("op")
    val operation: Operation,

    @JsonProperty("before")
    val before: Any?,

    @JsonProperty("after")
    val after: Any?,

    @JsonProperty("source")
    val source: Source,

    @JsonProperty("ts_ms")
    val timestamp: Long?,

    @JsonProperty("transaction")
    val transaction: Transaction?
) {
    enum class Operation {
        @JsonProperty("r")
        READ,
        @JsonProperty("t")
        TRUNCATE,

        @JsonProperty("m")
        MESSAGE,

        @JsonProperty("c")
        CREATE,

        @JsonProperty("u")
        UPDATE,

        @JsonProperty("d")
        DELETE;
    }

    data class Source(
        @JsonProperty("version")
        val version: String,

        @JsonProperty("connector")
        val connector: String,

        @JsonProperty("name")
        val name: String,

        @JsonProperty("ts_ms")
        val timestamp: Long,

        @JsonProperty("snapshot")
        val snapshot: String?,

        @JsonProperty("db")
        val db: String,

        @JsonProperty("schema")
        val schema: String,

        @JsonProperty("table")
        val table: String,

        @JsonProperty("txId")
        val txId: Long?
    )

    data class Transaction(
        @JsonProperty("id")
        val id: String,

        @JsonProperty("total_order")
        val totalOrder: Int,

        @JsonProperty("data_collection_order")
        val collectionOrder: Int
    )
}
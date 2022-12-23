package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.EventMetadata

data class DebeziumPsqlWrapperValue<T>(
    @JsonProperty("op")
    val operation: Operation,

    @JsonProperty("before")
    val before: T?,

    @JsonProperty("after")
    val after: T?,

    @JsonProperty("source")
    val source: Source,

    @JsonProperty("ts_ms")
    val timestamp: Long,

    @JsonProperty("transaction")
    val transaction: Transaction?
) {

    fun toEventMetadata() = EventMetadata(
        txId = transaction!!.id,
        txTotalOrder = transaction.totalOrder,
        txCollectionOrder = transaction.collectionOrder,
    )

    enum class Operation(
        private val value: String
    ) {
        READ("r"),
        CREATE("c"),
        UPDATE("u"),
        DELETE("d");

        @JsonValue
        fun getValue(): String {
            return value
        }
    }

    data class Source(
        @JsonProperty("txId")
        val txId: Long,

        @JsonProperty("table")
        val table: String
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
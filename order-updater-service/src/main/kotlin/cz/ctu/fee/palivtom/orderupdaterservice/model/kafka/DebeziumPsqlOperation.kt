package cz.ctu.fee.palivtom.orderupdaterservice.model.kafka

import com.fasterxml.jackson.annotation.JsonValue

enum class DebeziumPsqlOperation(
    private val value: String,
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
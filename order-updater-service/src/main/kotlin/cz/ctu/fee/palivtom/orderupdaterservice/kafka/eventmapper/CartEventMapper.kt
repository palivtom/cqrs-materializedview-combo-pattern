package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.kafka.model.CartValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper.toEventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val supportedOperations = hashSetOf(
    TransactionGroupValue.Operation.CREATE,
    TransactionGroupValue.Operation.UPDATE
)

@Component
class CartEventMapper(
    private val objectMapper: ObjectMapper,
    @Value("\${event.collections.cart:carts}") private val collectionName: String
) : RawEventMapper {
    override fun supports(rawEvent: TransactionGroupValue.Event): Boolean {
        return rawEvent.source.table == collectionName &&
                supportedOperations.contains(rawEvent.operation)
    }

    override fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event {
        val after = objectMapper.convertValue<CartValue>(rawEvent.after!!)

        return when (rawEvent.operation) {
            TransactionGroupValue.Operation.CREATE -> CreateCartEvent(
                id = after.id,
                userId = after.userId!!,
                exportedAt = after.exportedAt,
                eventMetadata = rawEvent.toEventMetadata()
            )

            TransactionGroupValue.Operation.UPDATE -> UpdateCartEvent(
                id = after.id,
                userId = after.userId!!,
                exportedAt = after.exportedAt,
                eventMetadata = rawEvent.toEventMetadata()
            )

            else -> throw NotImplementedError()
        }
    }
}
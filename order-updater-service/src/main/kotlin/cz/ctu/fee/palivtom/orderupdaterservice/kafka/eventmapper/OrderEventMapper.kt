package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.CreateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.UpdateOrderEvent
import cz.ctu.fee.palivtom.orderupdaterservice.kafka.model.OrderValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper.toEventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val supportedOperations = hashSetOf(
    TransactionGroupValue.Operation.CREATE,
    TransactionGroupValue.Operation.UPDATE
)

@Component
class OrderEventMapper(
    private val objectMapper: ObjectMapper,
    @Value("\${event.collections.order:orders}") private val collectionName: String
) : RawEventMapper {
    override fun supports(rawEvent: TransactionGroupValue.Event): Boolean {
        return rawEvent.source.table == collectionName &&
                supportedOperations.contains(rawEvent.operation)
    }

    override fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event {
        val after = objectMapper.convertValue<OrderValue>(rawEvent.after!!)

        return when (rawEvent.operation) {
            TransactionGroupValue.Operation.CREATE -> CreateOrderEvent(
                id = after.id,
                userId = after.userId!!,
                cartId = after.cartId!!,
                createdAt = after.createdAt!!,
                updatedAt = after.updatedAt,
                deletedAt = after.deletedAt,
                eventMetadata = rawEvent.toEventMetadata()
            )

            TransactionGroupValue.Operation.UPDATE -> UpdateOrderEvent(
                id = after.id,
                userId = after.userId!!,
                cartId = after.cartId!!,
                createdAt = after.createdAt!!,
                updatedAt = after.updatedAt,
                deletedAt = after.deletedAt,
                eventMetadata = rawEvent.toEventMetadata()
            )

            else -> throw NotImplementedError()
        }
    }
}
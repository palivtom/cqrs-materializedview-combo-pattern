package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.kafka.model.CartItemCartValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper.toEventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val supportedOperations = hashSetOf(
    TransactionGroupValue.Operation.CREATE,
    TransactionGroupValue.Operation.DELETE
)

@Component
class CartItemCartEventMapper(
    private val objectMapper: ObjectMapper,
    @Value("\${event.collections.cart-item_cart:cart_items_carts}") private val collectionName: String
) : RawEventMapper {
    override fun supports(rawEvent: TransactionGroupValue.Event): Boolean {
        return rawEvent.source.table == collectionName &&
                supportedOperations.contains(rawEvent.operation)
    }

    override fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event {
        return when (rawEvent.operation) {
            TransactionGroupValue.Operation.CREATE -> {
                val after = objectMapper.convertValue<CartItemCartValue>(rawEvent.after!!)
                CreateCartItemCartEvent(
                    cartItemId = after.cartItemId,
                    cartId = after.cartId!!,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            TransactionGroupValue.Operation.DELETE -> {
                val before = objectMapper.convertValue<CartItemCartValue>(rawEvent.before!!)
                DeleteCartItemCartEvent(
                    cartItemId = before.cartItemId,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            else -> throw NotImplementedError()
        }
    }
}
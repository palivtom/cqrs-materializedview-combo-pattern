package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.kafka.model.CartItemValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper.toEventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CartItemEventMapper(
    private val objectMapper: ObjectMapper,
    @Value("\${event.collections.cart-item:cart_items}") private val collectionName: String
) : RawEventMapper {
    override fun supports(rawEvent: TransactionGroupValue.Event): Boolean {
        return rawEvent.source.table == collectionName
    }

    override fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event {
        return when (rawEvent.operation) {
            TransactionGroupValue.Operation.CREATE -> {
                val after = objectMapper.convertValue<CartItemValue>(rawEvent.after!!)
                CreateCartItemEvent(
                    id = after.id,
                    productNo = after.productNo!!,
                    originalPrice = after.originalPrice!!,
                    discountPrice = after.discountPrice,
                    quantity = after.quantity!!,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            TransactionGroupValue.Operation.UPDATE -> {
                val after = objectMapper.convertValue<CartItemValue>(rawEvent.after!!)
                UpdateCartItemEvent(
                    id = after.id,
                    productNo = after.productNo!!,
                    originalPrice = after.originalPrice!!,
                    discountPrice = after.discountPrice,
                    quantity = after.quantity!!,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            TransactionGroupValue.Operation.DELETE -> {
                val before = objectMapper.convertValue<CartItemValue>(rawEvent.before!!)
                DeleteCartItemEvent(
                    id = before.id,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }
        }
    }
}
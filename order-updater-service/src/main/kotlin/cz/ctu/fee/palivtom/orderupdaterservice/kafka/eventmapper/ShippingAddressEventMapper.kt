package cz.ctu.fee.palivtom.orderupdaterservice.kafka.eventmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import cz.ctu.fee.palivtom.orderupdaterservice.model.event.*
import cz.ctu.fee.palivtom.orderupdaterservice.kafka.model.ShippingAddressValue
import cz.ctu.fee.palivtom.orderupdaterservice.utils.mapper.EventMetadataMapper.toEventMetadata
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ShippingAddressEventMapper(
    private val objectMapper: ObjectMapper,
    @Value("\${event.collections.shipping-address:shipping_addresses}") private val collectionName: String
) : RawEventMapper {
    override fun supports(rawEvent: TransactionGroupValue.Event): Boolean {
        return rawEvent.source.table == collectionName
    }

    override fun mapToEvent(rawEvent: TransactionGroupValue.Event): Event {
        return when (rawEvent.operation) {
            TransactionGroupValue.Operation.CREATE -> {
                val after = objectMapper.convertValue<ShippingAddressValue>(rawEvent.after!!)
                CreateShippingAddressEvent(
                    id = after.id,
                    orderId = after.orderId!!,
                    country = after.country!!,
                    city = after.city!!,
                    zipCode = after.zipCode!!,
                    street = after.street!!,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            TransactionGroupValue.Operation.UPDATE -> {
                val after = objectMapper.convertValue<ShippingAddressValue>(rawEvent.after!!)
                UpdateShippingAddressEvent(
                    id = after.id,
                    orderId = after.orderId!!,
                    country = after.country!!,
                    city = after.city!!,
                    zipCode = after.zipCode!!,
                    street = after.street!!,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }

            TransactionGroupValue.Operation.DELETE -> {
                val before = objectMapper.convertValue<ShippingAddressValue>(rawEvent.before!!)
                DeleteShippingAddressEvent(
                    id = before.id,
                    eventMetadata = rawEvent.toEventMetadata()
                )
            }
        }
    }
}
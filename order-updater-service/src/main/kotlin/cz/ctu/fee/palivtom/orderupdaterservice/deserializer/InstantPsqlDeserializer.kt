package cz.ctu.fee.palivtom.orderupdaterservice.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.Instant
import java.util.concurrent.TimeUnit

/**
 * Instant has a nanoseconds accuracy, and Postgresql stores timestamp in microseconds' accuracy.
 *
 * While serialization, JPA does the conversion for us. However, deserialization has to be done by ourselves.
 */
class InstantPsqlDeserializer(vc: Class<*>? = null) : StdDeserializer<Instant>(vc) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
        return Instant.ofEpochSecond(
            0L,
            TimeUnit.MICROSECONDS.toNanos(
                p.longValue
            )
        )
    }
}
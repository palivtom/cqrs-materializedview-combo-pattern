package cz.ctu.fee.palivtom.orderupdaterservice.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.Instant
import java.util.concurrent.TimeUnit

class InstantNanoDeserializer(vc: Class<*>?) : StdDeserializer<Instant>(vc) {
    constructor() : this(null)

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
        return Instant.ofEpochSecond(
            0L,
            TimeUnit.MICROSECONDS.toNanos(
                p.longValue
            )
        )
    }

}
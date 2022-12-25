package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

open class OrderServiceRuntimeException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
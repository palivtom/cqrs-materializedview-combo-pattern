package cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime

open class OrderViewServiceRuntimeException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
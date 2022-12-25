package cz.ctu.fee.palivtom.orderservice.exceptions

open class OrderServiceException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
package cz.ctu.fee.palivtom.orderviewservice.exceptions

open class OrderViewServiceException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
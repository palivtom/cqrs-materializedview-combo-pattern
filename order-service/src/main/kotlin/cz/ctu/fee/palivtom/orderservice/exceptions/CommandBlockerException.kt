package cz.ctu.fee.palivtom.orderservice.exceptions

class CommandBlockerException(
    message: String,
    cause: Throwable? = null
) : OrderServiceException(message, cause)
package cz.ctu.fee.palivtom.orderservice.exceptions

class AccessTokenException(
    message: String,
    cause: Throwable? = null
) : OrderServiceException(message, cause)
package cz.ctu.fee.palivtom.orderviewservice.exceptions

class AccessTokenException(
    message: String,
    cause: Throwable? = null
) : OrderViewServiceException(message, cause)
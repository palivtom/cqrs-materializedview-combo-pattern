package cz.ctu.fee.palivtom.security

import java.lang.Exception

class AccessTokenException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
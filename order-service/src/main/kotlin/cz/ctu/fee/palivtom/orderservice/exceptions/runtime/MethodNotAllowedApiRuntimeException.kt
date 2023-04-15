package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

import org.springframework.http.HttpStatus

class MethodNotAllowedApiRuntimeException(
    message: String
) : ApiRuntimeException(message, HttpStatus.METHOD_NOT_ALLOWED)
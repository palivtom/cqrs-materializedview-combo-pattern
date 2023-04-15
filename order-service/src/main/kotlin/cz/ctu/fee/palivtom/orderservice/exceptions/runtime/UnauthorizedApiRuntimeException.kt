package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

import org.springframework.http.HttpStatus

class UnauthorizedApiRuntimeException(
    message: String
) : ApiRuntimeException(message, HttpStatus.UNAUTHORIZED)
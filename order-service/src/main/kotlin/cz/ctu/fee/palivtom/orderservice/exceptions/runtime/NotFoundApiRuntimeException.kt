package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

import org.springframework.http.HttpStatus

class NotFoundApiRuntimeException(
    message: String
) : ApiRuntimeException(message, HttpStatus.NOT_FOUND)
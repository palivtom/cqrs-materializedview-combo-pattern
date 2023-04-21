package cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime

import org.springframework.http.HttpStatus

class NotFoundApiRuntimeException(
    message: String
) : ApiRuntimeException(message, HttpStatus.NOT_FOUND)
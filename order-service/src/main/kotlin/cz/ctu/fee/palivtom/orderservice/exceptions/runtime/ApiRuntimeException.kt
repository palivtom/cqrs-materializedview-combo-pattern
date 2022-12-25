package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

import org.springframework.http.HttpStatus

open class ApiRuntimeException(
    message: String,
    val httpStatus: HttpStatus
) : OrderServiceRuntimeException(message)
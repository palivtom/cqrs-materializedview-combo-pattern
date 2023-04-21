package cz.ctu.fee.palivtom.orderviewservice.exceptions.runtime

import org.springframework.http.HttpStatus

open class ApiRuntimeException(
    message: String,
    val httpStatus: HttpStatus
) : OrderViewServiceRuntimeException(message)
package cz.ctu.fee.palivtom.orderservice.config

import cz.ctu.fee.palivtom.orderservice.exceptions.AccessTokenException
import cz.ctu.fee.palivtom.orderservice.exceptions.runtime.ApiRuntimeException
import cz.ctu.fee.palivtom.orderservice.model.ExceptionDto
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(ApiRuntimeException::class)
    fun handleApiRuntimeException(e: ApiRuntimeException): ResponseEntity<ExceptionDto> {
        logger.warn("Api exception ${e.httpStatus}.", e)

        return ResponseEntity.status(e.httpStatus)
            .body(
                ExceptionDto(
                    code = e.httpStatus.value(),
                    title = e.httpStatus.toString(),
                    message = e.message
                )
            )
    }
    @ExceptionHandler(AccessTokenException::class)
    fun handleAccessTokenException(e: AccessTokenException): ResponseEntity<ExceptionDto> {
        val httpStatus = HttpStatus.FORBIDDEN
        return ResponseEntity.status(httpStatus)
            .body(
                ExceptionDto(
                    code = httpStatus.value(),
                    title = httpStatus.toString(),
                    message = e.message
                )
            )
    }
}
package cz.ctu.fee.palivtom.orderservice.exceptions.runtime

import cz.ctu.fee.palivtom.orderservice.model.ExceptionDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiRuntimeExceptionHandler {

    @ExceptionHandler(ApiRuntimeException::class)
    fun handleApiRuntimeException(e: ApiRuntimeException): ResponseEntity<ExceptionDto> {
        return ResponseEntity.status(e.httpStatus)
            .body(
                ExceptionDto(
                    code = e.httpStatus.value(),
                    title = e.httpStatus.toString(),
                    message = e.message
                )
            )
    }
}
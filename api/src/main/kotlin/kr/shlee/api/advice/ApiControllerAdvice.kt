package kr.shlee.api.advice

import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.common.error.PointException
import kr.shlee.domain.common.error.WaitlistException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(EventException::class)
    fun eventExceptionHandler(e: EventException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorResult.status)
            .body(ErrorResponse(e.errorResult.name, e.errorResult.message))
    }

    @ExceptionHandler(PointException::class)
    fun pointExceptionHandler(e: PointException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorResult.status)
            .body(ErrorResponse(e.errorResult.name, e.errorResult.message))
    }

    @ExceptionHandler(WaitlistException::class)
    fun waitlistExceptionExceptionHandler(e: WaitlistException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorResult.status)
            .body(ErrorResponse(e.errorResult.name, e.errorResult.message))
    }

    data class ErrorResponse(
        val code: String,
        val message: String
    )
}
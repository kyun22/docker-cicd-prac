package advice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(WaitlistException::class)
    fun waitlistExceptionHandler(e: WaitlistException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorResult.status)
            .body(ErrorResponse(e.errorResult.name, e.errorResult.message))
    }

    data class ErrorResponse(
        val code: String,
        val message: String
    )
}
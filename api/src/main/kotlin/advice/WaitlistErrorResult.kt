package advice

import org.springframework.http.HttpStatus

enum class WaitlistErrorResult (
    val status: HttpStatus,
    val message: String,
) {
    EVENT_NOT_EXISTS(HttpStatus.NOT_FOUND, "이벤트가 존재하지 않습니다."),
}
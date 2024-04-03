package advice

import org.springframework.http.HttpStatus

enum class EventErrorResult(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")

}
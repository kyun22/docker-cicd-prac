package kr.shlee.domain.common.error

import org.springframework.http.HttpStatus

class EventException(
    val errorResult: EventErrorResult
) : RuntimeException() {
    enum class EventErrorResult(
        val status: HttpStatus,
        val message: String,
    ) {
        INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
        MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
        RESULT_IS_EMPTY(HttpStatus.NOT_FOUND, "조회 결과가 없습니다.")

    }
}



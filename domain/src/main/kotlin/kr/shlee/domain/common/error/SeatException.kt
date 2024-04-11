package kr.shlee.domain.common.error

import org.springframework.http.HttpStatus

class SeatException(
    val errorResult: SeatErrorResult
) : RuntimeException() {
    enum class SeatErrorResult(
        val status: HttpStatus,
        val message: String,
    ) {
        NOT_AVAILABLE_SEATS(HttpStatus.BAD_REQUEST, "예약가능한 좌석이 아닙니다."),
    }
}

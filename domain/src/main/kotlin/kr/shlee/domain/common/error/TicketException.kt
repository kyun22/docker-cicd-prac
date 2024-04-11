package kr.shlee.domain.common.error

import org.springframework.http.HttpStatus

class TicketException(
    val errorResult: TicketErrorResult
) : RuntimeException() {
    enum class TicketErrorResult(
        val status: HttpStatus,
        val message: String,
    ) {
        TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "티켓을 찾지 못했습니다."),
        USER_POINT_NOT_ENOUGH(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 포인트가 부족합니다."),
        NOT_TICKET_OWNER(HttpStatus.INTERNAL_SERVER_ERROR, "해당 티켓을 예약한 유저가 아닙니다."),
    }
}
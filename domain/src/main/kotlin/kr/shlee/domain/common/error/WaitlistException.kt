package kr.shlee.domain.common.error

import org.springframework.http.HttpStatus


class WaitlistException (
    val errorResult: WaitlistErrorResult
): RuntimeException() {
    enum class WaitlistErrorResult (
        val status: HttpStatus,
        val message: String,
    ) {
        USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저"),
        UNREGISTERED_USER(HttpStatus.BAD_REQUEST, "아직 대기열에 등록하지 않은 유저")
    }
}
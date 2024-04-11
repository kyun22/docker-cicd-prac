package kr.shlee.domain.common.error

import org.springframework.http.HttpStatus

class UserException(
    val errorResult: UserErrorResult
) : RuntimeException() {

    enum class UserErrorResult(
        val status: HttpStatus,
        val message: String
    ) {
        LACK_OF_POINT(HttpStatus.BAD_REQUEST, "잔여 포인트보다 많은 포인트를 차감할 수 없습니다."),
        USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저")
    }
}
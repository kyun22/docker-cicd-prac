package kr.shlee.api.advice

import org.springframework.http.HttpStatus

enum class PointErrorResult (
    val status: HttpStatus,
    val message: String,
) {
    USER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),

}
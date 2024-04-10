package kr.shlee.domain.common.error

class UserException(
    val errorResult: UserErrorResult
) : RuntimeException() {

    enum class UserErrorResult(
        val message: String
    ) {
        LACK_OF_POINT("잔여 포인트보다 많은 포인트를 차감할 수 없습니다.")
    }
}
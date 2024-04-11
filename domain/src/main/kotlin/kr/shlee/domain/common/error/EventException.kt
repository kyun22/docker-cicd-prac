package kr.shlee.domain.common.error

class EventException(
    val errorResult: EventErrorResult
) : RuntimeException() {

    enum class EventErrorResult(
        val message: String
    ) {
        RESULT_IS_EMPTY("조회 결과가 없습니다."),
    }
}
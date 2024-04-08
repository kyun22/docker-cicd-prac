package kr.shlee.kr.shlee.advice

class EventException(
    val errorResult: EventErrorResult
): RuntimeException() {
}



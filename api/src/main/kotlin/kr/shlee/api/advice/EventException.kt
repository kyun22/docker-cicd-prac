package kr.shlee.api.advice

import kr.shlee.api.advice.EventErrorResult

class EventException(
    val errorResult: EventErrorResult
): RuntimeException() {
}



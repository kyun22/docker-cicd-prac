package kr.shlee.advice

import kr.shlee.advice.EventErrorResult

class EventException(
    val errorResult: EventErrorResult
): RuntimeException() {
}



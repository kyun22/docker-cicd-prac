package advice

class EventException(
    val errorResult: EventErrorResult
): RuntimeException() {
}



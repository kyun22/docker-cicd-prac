package advice

class WaitlistException(
    val errorResult: WaitlistErrorResult
): RuntimeException() {
}



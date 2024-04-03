package advice

class PointException (
    val errorResult: PointErrorResult
): RuntimeException() {
}
package kr.shlee.kr.shlee.advice

class PointException (
    val errorResult: PointErrorResult
): RuntimeException() {
}
package kr.shlee.kr.shlee.advice

class WaitlistException (
    val errorResult: WaitlistErrorResult
): RuntimeException() {
}
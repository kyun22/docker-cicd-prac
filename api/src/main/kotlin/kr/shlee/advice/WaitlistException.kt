package kr.shlee.advice

import kr.shlee.advice.WaitlistErrorResult

class WaitlistException (
    val errorResult: WaitlistErrorResult
): RuntimeException() {
}
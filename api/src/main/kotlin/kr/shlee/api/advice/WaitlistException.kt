package kr.shlee.api.advice

import kr.shlee.api.advice.WaitlistErrorResult

class WaitlistException (
    val errorResult: WaitlistErrorResult
): RuntimeException() {
}
package kr.shlee.api.advice

import kr.shlee.api.advice.PointErrorResult

class PointException (
    val errorResult: PointErrorResult
): RuntimeException() {
}
package kr.shlee.advice

import kr.shlee.advice.PointErrorResult

class PointException (
    val errorResult: PointErrorResult
): RuntimeException() {
}
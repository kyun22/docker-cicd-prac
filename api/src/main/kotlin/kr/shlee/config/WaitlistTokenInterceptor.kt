package kr.shlee.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.shlee.advice.EventErrorResult
import kr.shlee.advice.EventException
import kr.shlee.waitlist.components.WaitlistTokenValidator
import org.springframework.web.servlet.HandlerInterceptor

class WaitlistTokenInterceptor(
    val tokenValidator: WaitlistTokenValidator
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tokenString = request.getHeader("X-USER-TOKEN")
        if (!tokenValidator.validate(tokenString))
            throw EventException(EventErrorResult.INVALID_TOKEN)
        return tokenValidator.validate(tokenString)
    }
}
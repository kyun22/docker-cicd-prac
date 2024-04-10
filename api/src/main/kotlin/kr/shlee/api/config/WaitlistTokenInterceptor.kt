package kr.shlee.api.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.shlee.api.advice.EventErrorResult
import kr.shlee.api.advice.EventException
import kr.shlee.domain.waitlist.component.WaitlistTokenValidator
import org.springframework.web.servlet.HandlerInterceptor

class WaitlistTokenInterceptor(
    val tokenValidator: WaitlistTokenValidator
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tokenString = request.getHeader("X-USER-TOKEN")
        if (!tokenValidator.validate(tokenString)) throw EventException(EventErrorResult.INVALID_TOKEN)
        return tokenValidator.validate(tokenString)
    }
}
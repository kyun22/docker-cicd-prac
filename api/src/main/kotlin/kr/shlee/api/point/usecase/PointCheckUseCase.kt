package kr.shlee.api.point.usecase

import kr.shlee.api.advice.PointErrorResult
import kr.shlee.api.advice.PointException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.springframework.stereotype.Component

@Component
class PointCheckUseCase(
    val userManager: UserManager
) {

    fun execute(userId: String): User {
        return userManager.find(userId) ?: throw PointException(PointErrorResult.USER_NOT_EXISTS)
    }
}

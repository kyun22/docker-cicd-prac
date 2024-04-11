package kr.shlee.api.point.usecase

import kr.shlee.domain.common.error.PointException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.springframework.stereotype.Component

@Component
class PointCheckUseCase(
    val userManager: UserManager
) {

    fun execute(userId: String): User {
        return userManager.find(userId) ?: throw PointException(PointException.PointErrorResult.USER_NOT_EXISTS)
    }
}

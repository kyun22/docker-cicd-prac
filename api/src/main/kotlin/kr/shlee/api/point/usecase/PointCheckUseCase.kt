package kr.shlee.api.point.usecase

import kr.shlee.api.advice.PointErrorResult
import kr.shlee.api.advice.PointException
import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class PointCheckUseCase(
    val userRepository: UserRepository
) {

    fun execute(userId: String): User {
        return userRepository.findById(userId) ?: throw PointException(PointErrorResult.USER_NOT_EXISTS)
    }
}

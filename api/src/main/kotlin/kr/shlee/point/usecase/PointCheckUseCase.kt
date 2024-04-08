package kr.shlee.point.usecase

import kr.shlee.advice.PointErrorResult
import kr.shlee.advice.PointException
import org.springframework.stereotype.Component
import kr.shlee.point.models.User
import kr.shlee.waitlist.repositories.UserRepository

@Component
class PointCheckUseCase(
    val userRepository: UserRepository
) {

    fun execute(userId: String): User {
        return userRepository.findById(userId) ?: throw PointException(PointErrorResult.USER_NOT_EXISTS)
    }
}

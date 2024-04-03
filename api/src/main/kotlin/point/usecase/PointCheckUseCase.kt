package point.usecase

import advice.PointErrorResult
import advice.PointException
import org.springframework.stereotype.Component
import waitlist.models.User
import waitlist.repositories.UserRepository

@Component
class PointCheckUseCase(
    val userRepository: UserRepository
) {

    fun execute(userId: String): User {
        return userRepository.findById(userId) ?: throw PointException(PointErrorResult.USER_NOT_EXISTS)
    }
}

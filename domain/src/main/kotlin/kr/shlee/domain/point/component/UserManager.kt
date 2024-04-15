package kr.shlee.domain.point.component

import jakarta.persistence.LockModeType
import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository
) {
    fun find(userId: String): User? {
        return userRepository.findById(userId)
    }

    fun get(userId: String): User {
        return userRepository.findById(userId)
            ?: throw UserException(UserException.UserErrorResult.USER_NOT_FOUND)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

}
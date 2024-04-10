package kr.shlee.domain.point.component

import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository
) {
    fun find(userId: String): User? {
        return userRepository.findById(userId)
    }

    fun save(user: User): User? {
        return userRepository.save(user)
    }

}
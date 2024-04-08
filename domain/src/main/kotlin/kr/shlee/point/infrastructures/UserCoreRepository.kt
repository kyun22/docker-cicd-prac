package kr.shlee.point.infrastructures

import kr.shlee.point.models.User
import kr.shlee.point.repositories.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    val userJpaRepository: UserJpaRepository
): UserRepository {
    override fun findById(userId: String): User? {
        return userJpaRepository.findById(userId).orElse(null)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }
}
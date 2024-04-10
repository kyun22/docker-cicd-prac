package kr.shlee.domain.point.infrastructure

import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
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
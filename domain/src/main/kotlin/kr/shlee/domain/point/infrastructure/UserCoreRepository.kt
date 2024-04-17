package kr.shlee.domain.point.infrastructure

import jakarta.persistence.LockModeType
import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    val userJpaRepository: UserJpaRepository
): UserRepository {
    override fun findById(userId: String): User? {
        return userJpaRepository.findByIdOrNull(userId)
    }

    override fun findByIdWithLock(userId: String): User? {
        return userJpaRepository.findByIdOrNull(userId)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }

    override fun deleteAll() {
        userJpaRepository.deleteAll()
    }
}
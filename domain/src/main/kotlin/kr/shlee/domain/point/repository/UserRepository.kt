package kr.shlee.domain.point.repository

import jakarta.persistence.LockModeType
import kr.shlee.domain.point.model.User
import org.springframework.data.jpa.repository.Lock

interface UserRepository {
    fun findById(userId: String): User?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByIdWithLock(userId: String): User?
    fun save(user: User): User
    fun deleteAll()

}
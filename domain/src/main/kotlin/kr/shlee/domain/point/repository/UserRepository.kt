package kr.shlee.domain.point.repository

import jakarta.persistence.LockModeType
import kr.shlee.domain.point.model.User
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface UserRepository {
    fun findById(userId: String): User?

    fun findByIdWithLock(userId: String): User?

    fun save(user: User): User
    fun deleteAll()

}
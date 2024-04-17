package kr.shlee.domain.point.infrastructure

import jakarta.persistence.LockModeType
import kr.shlee.domain.point.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserJpaRepository : JpaRepository<User, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :userId")
    fun findByIdForUpdate(@Param("userId") userId: String): User?
}
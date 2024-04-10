package kr.shlee.domain.point.infrastructure

import kr.shlee.domain.point.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, String> {
}
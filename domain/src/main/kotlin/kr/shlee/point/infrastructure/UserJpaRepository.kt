package kr.shlee.point.infrastructure

import kr.shlee.point.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, String> {
}
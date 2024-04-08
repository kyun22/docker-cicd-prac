package kr.shlee.point.infrastructures

import kr.shlee.point.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, String> {
}
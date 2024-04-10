package kr.shlee.domain.waitlist.infrastructure

import kr.shlee.domain.waitlist.model.Waitlist
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WaitlistJpaRepository: JpaRepository<Waitlist, Long> {
    fun findByUserId(userId: String) : Optional<Waitlist>
    fun findByToken(token: String): Optional<Waitlist>
}
package kr.shlee.waitlist.infrastructures

import org.springframework.data.jpa.repository.JpaRepository
import kr.shlee.waitlist.models.Waitlist
import java.util.*

interface WaitlistJpaRepository: JpaRepository<Waitlist, Long> {
    fun findByUserId(userId: String) : Optional<Waitlist>
    fun findByToken(token: String): Optional<Waitlist>
}
package kr.shlee.waitlist.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import kr.shlee.waitlist.models.Waitlist

interface WaitlistJpaRepository: JpaRepository<Waitlist, String> {
    fun findByUserIdAndEventId(userId: String, eventId: String): Waitlist
}
package kr.shlee.waitlist.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import kr.shlee.waitlist.models.Event

interface EventJpaRepository: JpaRepository<Event, String> {
    fun findByEventId(eventId: String): List<Event>
}
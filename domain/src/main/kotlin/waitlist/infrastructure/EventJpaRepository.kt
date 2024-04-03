package waitlist.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import waitlist.models.Event

interface EventJpaRepository: JpaRepository<Event, String> {
    fun findByEventId(eventId: String): List<Event>
}
package kr.shlee.domain.event.infrastructure

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class EventCoreRepository(
    private val eventJpaRepository: EventJpaRepository,
    private val eventCustomRepository: EventCustomRepository
) : EventRepository {
    override fun findById(eventId: String): Event? {
        return eventJpaRepository.findById(eventId).orElse(null)
    }

    override fun findByDate(date: LocalDate): List<Event> {
        return eventCustomRepository.findByDate(date)
    }

    override fun findAll(): List<Event> {
        return eventJpaRepository.findAll()
    }

}
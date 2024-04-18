package kr.shlee.domain.event.infrastructure

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class EventCoreRepository(
    private val eventJpaRepository: EventJpaRepository,
    private val eventCustomRepository: EventCustomRepository
) : EventRepository {
    override fun findById(eventId: String): Event? {
        return eventJpaRepository.findByIdOrNull(eventId)
    }

    override fun findByDate(date: LocalDate): List<Event> {
        return eventCustomRepository.findByDate(date)
    }

    override fun findAll(): List<Event> {
        return eventJpaRepository.findAll()
    }

    override fun save(event: Event): Event {
        return eventJpaRepository.save(event)
    }

}
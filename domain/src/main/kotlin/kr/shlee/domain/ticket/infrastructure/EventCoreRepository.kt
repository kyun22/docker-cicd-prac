package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Event
import kr.shlee.domain.ticket.repository.EventRepository
import org.springframework.stereotype.Repository

@Repository
class EventCoreRepository(
    val eventJpaRepository: EventJpaRepository
) : EventRepository {
    override fun findById(eventId: String): Event? {
        return eventJpaRepository.findById(eventId).orElse(null)
    }

    override fun findByDate(date: String): List<Event> {
        TODO("Not yet implemented")
    }


    override fun findAll(): List<Event> {
        return eventJpaRepository.findAll()
    }

}
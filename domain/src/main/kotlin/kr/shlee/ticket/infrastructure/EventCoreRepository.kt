package kr.shlee.ticket.infrastructure

import org.springframework.stereotype.Repository
import kr.shlee.ticket.model.Event
import kr.shlee.ticket.repository.EventRepository

@Repository
class EventCoreRepository(
    val eventJpaRepository: EventJpaRepository
) : EventRepository {
    override fun findById(id: String): Event? {
        return eventJpaRepository.findById(id).orElse(null)
    }

    override fun findByDate(date: String): List<Event> {
        TODO("Not yet implemented")
    }


    override fun findAll(): List<Event> {
        return eventJpaRepository.findAll()
    }

}
package kr.shlee.waitlist.infrastructure

import org.springframework.stereotype.Repository
import kr.shlee.waitlist.models.Event
import kr.shlee.waitlist.repositories.EventRepository
import java.util.*

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

    // todo, 이거 지우고 id로 찾는 api 새로 만들?
    override fun findByEventId(eventId: String): List<Event> {
        return eventJpaRepository.findByEventId(eventId)
    }

    override fun findAll(): List<Event> {
        return eventJpaRepository.findAll()
    }

}
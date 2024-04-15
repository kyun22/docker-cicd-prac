package kr.shlee.domain.event.component

import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.common.util.DateUtils
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import org.springframework.stereotype.Component

@Component
class EventFinder(
    private val eventRepository: EventRepository
) {
    fun findAll(): List<Event> {
        val events = eventRepository.findAll()
        if (events.isEmpty()) throw EventException(EventException.EventErrorResult.RESULT_IS_EMPTY)
        return events
    }

    fun find(eventId: String): Event {
        return eventRepository.findById(eventId) ?: throw EventException(EventException.EventErrorResult.RESULT_IS_EMPTY)
    }

    fun findByDate(dateString: String): List<Event> {
        val events = eventRepository.findByDate(DateUtils.convertStringToLocalDate(dateString))
        if (events.isEmpty()) throw EventException(EventException.EventErrorResult.RESULT_IS_EMPTY)
        return events
    }
}
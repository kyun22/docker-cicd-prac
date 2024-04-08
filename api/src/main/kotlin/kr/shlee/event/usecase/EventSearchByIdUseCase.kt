package kr.shlee.event.usecase

import kr.shlee.advice.EventErrorResult
import kr.shlee.advice.EventException
import kr.shlee.event.dto.EventResponse
import kr.shlee.ticket.repositories.EventRepository
import org.springframework.stereotype.Component

@Component
class EventSearchByIdUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?, eventId: String): EventResponse {

        val event = eventRepository.findById(eventId) ?: throw EventException(EventErrorResult.EVENT_NOT_FOUND)

        return EventResponse.newOf(event)
    }

}
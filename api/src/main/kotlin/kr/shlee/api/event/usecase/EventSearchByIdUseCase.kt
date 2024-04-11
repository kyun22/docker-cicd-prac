package kr.shlee.api.event.usecase

import kr.shlee.api.advice.EventErrorResult
import kr.shlee.api.advice.EventException
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.api.event.dto.EventResponse
import org.springframework.stereotype.Component

@Component
class EventSearchByIdUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?, eventId: String): EventResponse {

        val event = eventRepository.findById(eventId) ?: throw EventException(EventErrorResult.EVENT_NOT_FOUND)

        return EventResponse.of(event)
    }

}
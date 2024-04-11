package kr.shlee.api.event.usecase

import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.api.event.dto.EventResponse
import kr.shlee.domain.common.error.EventException
import org.springframework.stereotype.Component

@Component
class EventSearchByIdUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?, eventId: String): EventResponse {

        val event = eventRepository.findById(eventId) ?: throw EventException(EventException.EventErrorResult.RESULT_IS_EMPTY)

        return EventResponse.of(event)
    }

}
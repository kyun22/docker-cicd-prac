package kr.shlee.api.event.usecase

import kr.shlee.domain.ticket.repository.EventRepository
import kr.shlee.api.event.dto.EventListResponse
import kr.shlee.api.event.dto.EventResponse
import org.springframework.stereotype.Component

@Component
class EventSearchUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?): List<EventResponse> {
        return EventListResponse.newOf(eventRepository.findAll()).toList()
    }

}

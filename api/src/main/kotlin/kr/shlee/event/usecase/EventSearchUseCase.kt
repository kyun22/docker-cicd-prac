package kr.shlee.event.usecase

import kr.shlee.event.dto.EventListResponse
import kr.shlee.event.dto.EventResponse
import kr.shlee.ticket.repositories.EventRepository
import org.springframework.stereotype.Component

@Component
class EventSearchUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?): List<EventResponse> {
        return EventListResponse.newOf(eventRepository.findAll()).toList()
    }

}

package kr.shlee.api.event.usecase

import kr.shlee.domain.ticket.repository.EventRepository
import kr.shlee.api.event.dto.EventListResponse
import kr.shlee.api.event.dto.EventResponse
import org.springframework.stereotype.Component

@Component
class EventSearchByDateUseCase(
    val eventRepository: EventRepository,
) {
    fun execute(token: String?, dateString: String): List<EventResponse> {

        // todo, dateString 파싱
        val events = eventRepository.findByDate(dateString)
        return EventListResponse.newOf(events).toList()
    }


}

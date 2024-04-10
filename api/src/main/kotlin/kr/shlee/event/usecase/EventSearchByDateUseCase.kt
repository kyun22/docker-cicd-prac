package kr.shlee.event.usecase

import kr.shlee.event.dto.EventListResponse
import kr.shlee.event.dto.EventResponse
import kr.shlee.ticket.repository.EventRepository
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

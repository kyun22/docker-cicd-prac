package kr.shlee.api.event.usecase

import kr.shlee.api.event.dto.EventListResponse
import kr.shlee.api.event.dto.EventResponse
import kr.shlee.domain.event.component.EventFinder
import org.springframework.stereotype.Component

@Component
class EventSearchByDateUseCase(
    val eventFinder: EventFinder
) {
    fun execute(token: String?, dateString: String): List<EventResponse> {
        val events = eventFinder.findByDate(dateString)
        return EventListResponse.of(events).toList()
    }
}

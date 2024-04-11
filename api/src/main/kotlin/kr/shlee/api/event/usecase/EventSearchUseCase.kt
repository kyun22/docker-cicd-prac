package kr.shlee.api.event.usecase

import kr.shlee.api.event.dto.EventListResponse
import kr.shlee.api.event.dto.EventResponse
import kr.shlee.domain.event.component.EventFinder
import org.springframework.stereotype.Component

@Component
class EventSearchUseCase(
    private val eventFinder: EventFinder,
) {
    fun execute(token: String?): List<EventResponse> {
        return EventListResponse.of(eventFinder.findAll()).toList()
    }

}

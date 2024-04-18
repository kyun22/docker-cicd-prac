package kr.shlee.api.event.usecase

import kr.shlee.api.event.dto.EventResponse
import kr.shlee.domain.event.component.EventFinder
import kr.shlee.domain.ticket.component.SeatFinder
import org.springframework.stereotype.Component

@Component
class EventSearchByIdUseCase(
    val eventFinder: EventFinder,
    val seatFinder: SeatFinder
) {
    operator fun invoke(eventId: String): EventResponse {
        // event를 가져온다
        val event = eventFinder.get(eventId)

        // 해당 이벤트의 seats를 가져온다
        val seats = seatFinder.findSeatsByEventId(eventId)

        return EventResponse.of(event, seats)
    }

}
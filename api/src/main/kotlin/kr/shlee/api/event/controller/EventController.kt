package kr.shlee.api.event.controller

import kr.shlee.api.event.dto.EventResponse
import kr.shlee.api.event.usecase.EventSearchByDateUseCase
import kr.shlee.api.event.usecase.EventSearchByIdUseCase
import kr.shlee.api.event.usecase.EventSearchUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(
    val eventSearchByIdUseCase: EventSearchByIdUseCase,
    val eventSearchByDateUseCase: EventSearchByDateUseCase,
    val eventSearchUseCase: EventSearchUseCase
) {
    @GetMapping("")
    fun searchAll(
        @RequestHeader("X-USER-TOKEN") token: String?,
    ): List<EventResponse> {
        return eventSearchUseCase()
    }

    @GetMapping("/{eventId}")
    fun searchById(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @PathVariable eventId: String
    ): EventResponse {
        return eventSearchByIdUseCase(eventId)
    }

    @GetMapping("/{eventId}/seats")
    fun searchSeatByEventId(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @PathVariable eventId: String
    ) {

    }

    @GetMapping("/dates/{dateString}")
    fun searchByDate(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @PathVariable dateString: String
    ): List<EventResponse> {
        return eventSearchByDateUseCase(dateString)
    }

}
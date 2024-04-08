package kr.shlee.event.controller

import kr.shlee.event.dto.EventResponse
import kr.shlee.event.usecase.EventSearchUseCase
import kr.shlee.event.usecase.EventSearchByDateUseCase
import kr.shlee.event.usecase.EventSearchByIdUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        return eventSearchUseCase.execute(token)
    }

    @GetMapping("/{eventId}")
    fun searchById(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @PathVariable eventId: String
    ): EventResponse {
        return eventSearchByIdUseCase.execute(token, eventId)
    }

    @GetMapping("/dates/{dateString}")
    fun searchByDate(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @PathVariable dateString: String
    ): List<EventResponse> {
        return eventSearchByDateUseCase.execute(token, dateString)
    }

}
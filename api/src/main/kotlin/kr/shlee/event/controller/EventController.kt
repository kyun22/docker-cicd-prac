package kr.shlee.event.controller

import kr.shlee.event.dto.EventResponse
import kr.shlee.event.usecase.EventSearchUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController (
    val eventSearchUseCase: EventSearchUseCase
) {

    @GetMapping("/events")
    fun search(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @RequestParam(required = false) date: String?,
        @RequestParam(required = false) eventId: String?
    ): List<EventResponse> {
        return eventSearchUseCase.execute(date, eventId, token)
    }
}
package ticket

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController (
    val eventSearchUseCase: EventSearchUseCase
) {

    @GetMapping("/events")
    fun search(
        @RequestParam(required = false) date: String?,
        @RequestParam(required = false) eventId: String?
    ): List<EventResponse> {
        return eventSearchUseCase.execute(date, eventId)
    }
}
package event.usecase

import advice.WaitlistErrorResult
import advice.WaitlistException
import event.dto.EventListResponse
import event.dto.EventResponse
import org.springframework.stereotype.Component
import waitlist.models.Waitlist
import waitlist.repositories.EventRepository
import waitlist.repositories.WaitListRepository

@Component
class EventSearchUseCase(
    val eventRepository: EventRepository,
    val waitListRepository: WaitListRepository
) {
    fun execute(date: String?, eventId: String?, token: String?): List<EventResponse> {
        // 토큰 유효성을 검증
        if (token != null) {
            val waitlist = waitListRepository.findById(token) ?: throw WaitlistException(WaitlistErrorResult.MISSING_TOKEN)
            if(waitlist.status != Waitlist.Status.AVAILABLE) throw WaitlistException(WaitlistErrorResult.INVALID_TOKEN)
        }

        val events = when {
            eventId != null -> eventRepository.findByEventId(eventId)
            date != null -> eventRepository.findByDate(date)
            else -> eventRepository.findAll()
        }

        return EventListResponse.newOf(events).toList()
    }

}
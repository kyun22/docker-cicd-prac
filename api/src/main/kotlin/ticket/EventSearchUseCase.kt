package ticket

import org.springframework.stereotype.Component
import waitlist.repositories.EventRepository

@Component
class EventSearchUseCase(
    val eventRepository: EventRepository
) {
    fun execute(date: String?, eventId: String?): List<EventResponse> {
        val events = when {
            eventId != null -> eventRepository.findByEventId(eventId)
            date != null -> eventRepository.findByDate(date)
            else -> eventRepository.findAll()
        }

        return EventListResponse.newOf(events).toList()
    }

}
package kr.shlee.kr.shlee.event.usecase

import kr.shlee.kr.shlee.advice.EventErrorResult
import kr.shlee.kr.shlee.advice.EventException
import kr.shlee.kr.shlee.event.dto.EventListResponse
import kr.shlee.kr.shlee.event.dto.EventResponse
import org.springframework.stereotype.Component
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.EventRepository
import kr.shlee.waitlist.repositories.WaitListRepository

@Component
class EventSearchUseCase(
    val eventRepository: EventRepository,
    val waitListRepository: WaitListRepository
) {
    fun execute(date: String?, eventId: String?, token: String?): List<EventResponse> {
        // 토큰 유효성을 검증
        if (token != null) {
            val waitlist = waitListRepository.findById(token) ?: throw EventException(EventErrorResult.MISSING_TOKEN)
            if(waitlist.status != Waitlist.Status.AVAILABLE) throw EventException(EventErrorResult.INVALID_TOKEN)
        }

        val events = when {
            eventId != null -> eventRepository.findByEventId(eventId)
            date != null -> eventRepository.findByDate(date)
            else -> eventRepository.findAll()
        }

        return EventListResponse.newOf(events).toList()
    }

}
package kr.shlee.event.usecase

import kr.shlee.advice.EventErrorResult
import kr.shlee.advice.EventException
import kr.shlee.event.dto.EventResponse
import org.springframework.stereotype.Component
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.EventRepository
import kr.shlee.waitlist.repositories.WaitListRepository

@Component
class EventSearchByIdUseCase(
    val eventRepository: EventRepository,
    val waitListRepository: WaitListRepository
) {
    fun execute(token: String?, eventId: String): EventResponse {
        // 토큰 유효성을 검증
        if (token != null) {
            val waitlist = waitListRepository.findById(token) ?: throw EventException(EventErrorResult.MISSING_TOKEN)
            if (waitlist.status != Waitlist.Status.AVAILABLE) throw EventException(EventErrorResult.INVALID_TOKEN)
        }

        val event = eventRepository.findById(eventId) ?: throw EventException(EventErrorResult.EVENT_NOT_FOUND)

        return EventResponse.newOf(event)
    }

}
package kr.shlee.event.usecase

import kr.shlee.advice.EventErrorResult
import kr.shlee.advice.EventException
import kr.shlee.event.dto.EventListResponse
import kr.shlee.event.dto.EventResponse
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.EventRepository
import kr.shlee.waitlist.repositories.WaitListRepository
import org.springframework.stereotype.Component

@Component
class EventSearchByDateUseCase(
    val eventRepository: EventRepository,
    val waitListRepository: WaitListRepository
) {
    fun execute(token: String?, dateString: String): List<EventResponse> {
        // 토큰 유효성을 검증
        if (token != null) {
            val waitlist = waitListRepository.findById(token) ?: throw EventException(EventErrorResult.MISSING_TOKEN)
            if (waitlist.status != Waitlist.Status.AVAILABLE) throw EventException(EventErrorResult.INVALID_TOKEN)
        }

        // todo, dateString 파싱
        val events = eventRepository.findByDate(dateString)
        return EventListResponse.newOf(events).toList()
    }


}

package kr.shlee.waitlist.usecase

import kr.shlee.advice.WaitlistErrorResult
import kr.shlee.advice.WaitlistException
import org.springframework.stereotype.Component
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.dto.WaitlistResponse
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.EventRepository
import kr.shlee.waitlist.repositories.WaitListRepository

@Component
class WaitlistRegisterUseCase(
    private val eventRepository: EventRepository,
    private val waitlistRepository: WaitListRepository
) {

    fun execute(request: WaitlistRequest): WaitlistResponse {
        // event가 존재하는지 체크
        val event = eventRepository.findById(request.eventId) ?: throw WaitlistException(WaitlistErrorResult.EVENT_NOT_EXISTS)

        // user가 존재하는지 체크

        // 이미 등록한 대기열이 있는지 체크
        val before = waitlistRepository.findByUserIdAndEventId(request.userId, request.eventId)
        // todo, 이미 존재하는 경우 처리해야함 ?.
        //  이미 발급된 토큰을 만료시간 갱신해서 주기

        // 대기열 순번 등록
        val waitlist = waitlistRepository.save(Waitlist.newOf(request.userId, event.id))
        // 상태 체크 : available, waiting
        // 대기열 순번 생성
        // 토큰 발급
        // 응답

        return WaitlistResponse.of(waitlist)
    }

}
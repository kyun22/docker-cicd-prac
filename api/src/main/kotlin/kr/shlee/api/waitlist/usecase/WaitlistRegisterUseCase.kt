package kr.shlee.api.waitlist.usecase

import kr.shlee.domain.waitlist.component.WaitlistWriter
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.api.waitlist.dto.WaitlistResponse
import org.springframework.stereotype.Component

@Component
class WaitlistRegisterUseCase(
    private val waitlistWriter: WaitlistWriter,
    private val waitlistReader: WaitlistReader,
) {
    private val maxAvailableCount: Long = 50L

    // todo, transactional 처리 : 전체로 걸고, 하위 컴포넌트로 줄여도될지 체크

    fun execute(request: WaitlistRequest.Register): WaitlistResponse.Register {
        // 이미 등록한 대기열이 있으면 그냥 그 토큰 응답
        waitlistReader.findAlreadyRegistered(request.userId)?.let { return WaitlistResponse.Register.of(it) }

        // 대기열 순번 등록
        val activeCount = waitlistReader.getAvailableWaitlistCount()
        val waitlist = waitlistWriter.add(request.userId, activeCount, maxAvailableCount)

        // 토큰 발급 응답
        return WaitlistResponse.Register.of(waitlist)
    }

}
package kr.shlee.api.waitlist.usecase

import kr.shlee.domain.waitlist.component.WaitlistWriter
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.api.waitlist.dto.WaitlistResponse
import kr.shlee.domain.point.component.UserManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WaitlistRegisterUseCase(
    private val userManager: UserManager,
    private val waitlistWriter: WaitlistWriter,
    private val waitlistReader: WaitlistReader,
) {
    private val maxAvailableCount: Long = 50L

    @Transactional
    operator fun invoke(request: WaitlistRequest.Register): WaitlistResponse.Register {
        // 이미 등록한 대기열이 있으면 그냥 그 토큰 응답
        val user = userManager.getWithLock(request.userId)  // 유저 존재하는지 체크
        waitlistReader.findAlreadyRegistered(user.id)
            ?.let { return WaitlistResponse.Register.of(it) }

        // 대기열 순번 등록
        val activeCount = waitlistReader.getAvailableWaitlistCount()
        val waitlist = waitlistWriter.add(user.id, activeCount, maxAvailableCount)

        // 토큰 발급 응답
        return WaitlistResponse.Register.of(waitlist)
    }

}
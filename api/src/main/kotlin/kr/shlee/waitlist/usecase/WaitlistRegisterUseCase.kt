package kr.shlee.waitlist.usecase

import kr.shlee.advice.WaitlistErrorResult
import kr.shlee.advice.WaitlistException
import kr.shlee.point.repositories.UserRepository
import kr.shlee.waitlist.components.WaitlistAppender
import kr.shlee.waitlist.components.WaitlistReader
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.dto.WaitlistResponse
import kr.shlee.waitlist.models.Waitlist
import org.springframework.stereotype.Component

@Component
class WaitlistRegisterUseCase(
    private val userRepository: UserRepository,
    private val waitlistAppender: WaitlistAppender,
    private val waitlistReader: WaitlistReader,
) {
    private val max_available_count: Long = 50L

    fun execute(request: WaitlistRequest.Register): WaitlistResponse.Register {
        // user가 존재하는지 체크
        userRepository.findById(request.userId) ?: throw WaitlistException(WaitlistErrorResult.USER_NOT_FOUND)

        // 이미 등록한 대기열이 있으면 그냥 그 토큰 응답
        waitlistReader.find(request.userId)?.let { return WaitlistResponse.Register.of(it) }

        // 대기열 순번 등록
        val waitlist = waitlistAppender.add(Waitlist.newOf(request.userId))

        // 상태 체크 : available, waiting
        if (waitlistReader.getAvailableCount() < max_available_count)
            waitlist.apply { changeStatus(Waitlist.Status.AVAILABLE) }

        // 토큰 발급 응답
        return WaitlistResponse.Register.of(waitlist)
    }

}
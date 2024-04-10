package kr.shlee.api.waitlist.usecase

import kr.shlee.api.advice.WaitlistErrorResult
import kr.shlee.api.advice.WaitlistException
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.api.waitlist.dto.WaitlistResponse
import org.springframework.stereotype.Component

@Component
class WaitlistCheckOrderUseCase(
    private val waitlistReader: WaitlistReader
) {
    fun execute(request: WaitlistRequest.Position): WaitlistResponse.Position {
        val waitlist =
            waitlistReader.findByToken(request.token) ?: throw WaitlistException(WaitlistErrorResult.UNREGISTERED_USER)

        val position = waitlistReader.getLastAvailableWaitlist()
            ?.let { waitlist.getPositionFromLastWaitlist(it) }
            ?: 0
        return WaitlistResponse.Position(position)
    }
}

package kr.shlee.waitlist.usecase

import kr.shlee.advice.WaitlistErrorResult
import kr.shlee.advice.WaitlistException
import kr.shlee.waitlist.components.WaitlistReader
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.dto.WaitlistResponse
import org.springframework.stereotype.Component

@Component
class WaitlistCheckOrderUseCase(
    private val waitlistReader: WaitlistReader
) {
    fun execute(request: WaitlistRequest.Position): WaitlistResponse.Position {
        val waitlist =
            waitlistReader.find(request.userId) ?: throw WaitlistException(WaitlistErrorResult.USER_NOT_FOUND)

        val position = waitlistReader.getLastAvailableWaitlist()
            ?.let { waitlist.getPositionFromLastWaitlist(it) }
            ?: 0
        return WaitlistResponse.Position(position)
    }
}

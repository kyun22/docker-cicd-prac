package kr.shlee.api.waitlist.usecase

import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.api.waitlist.dto.WaitlistResponse
import kr.shlee.domain.common.error.WaitlistException
import org.springframework.stereotype.Component

@Component
class WaitlistCheckOrderUseCase(
    private val waitlistReader: WaitlistReader
) {
    fun execute(request: WaitlistRequest.Position): WaitlistResponse.Position {
        val waitlist =
            waitlistReader.findByToken(request.token) ?: throw WaitlistException(WaitlistException.WaitlistErrorResult.UNREGISTERED_USER)

        val position = waitlistReader.getLastAvailableWaitlist()
            ?.let { waitlist.getPositionFromLastWaitlist(it) }
            ?: 0
        return WaitlistResponse.Position(position)
    }
}

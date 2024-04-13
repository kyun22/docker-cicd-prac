package kr.shlee.api.waitlist.usecase

import kr.shlee.domain.waitlist.component.WaitlistWriter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WaitlistScheduleExpireUseCase (
    private val waitlistWriter: WaitlistWriter
) {

    @Scheduled(fixedDelay = 60000)
    fun expireOldWaitlist() {
        waitlistWriter.expireAllOldWaitlist()
    }
}
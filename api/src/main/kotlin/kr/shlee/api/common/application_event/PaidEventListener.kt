package kr.shlee.api.common.application_event

import kr.shlee.domain.ticket.model.PaidEvent
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.domain.waitlist.component.WaitlistWriter
import kr.shlee.domain.waitlist.model.Waitlist
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaidEventListener(
    private val waitlistReader: WaitlistReader,
    private val waitlistWriter: WaitlistWriter
) {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun expireWaitlist(paidEvent: PaidEvent) {
        log.info("ticket payment is complete. process expire token. userId: {}", paidEvent.userId)

        // 결제 완료 만료시키기
        val paid = waitlistReader.getByUserId(paidEvent.userId)
        waitlistWriter.save(paid.apply { changeStatus(Waitlist.Status.EXPIRED) })

        // 다음 순서 입장시키기
        val waiting = waitlistReader.findFirstWaitingWaitlist() ?: return
        waitlistWriter.save(waiting.apply { changeStatus(Waitlist.Status.AVAILABLE) })
    }
}
package kr.shlee.domain.waitlist.component

import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistWriter(
    val waitListRepository: WaitListRepository
) {
    fun save(waitlist: Waitlist): Waitlist {
        return waitListRepository.save(waitlist)
    }

    fun add(userId: String, activeCount: Long, maxAvailableCount: Long): Waitlist {
        val waitlist = Waitlist.newOf(userId)
        if (activeCount < maxAvailableCount)
            waitlist.changeStatus(Waitlist.Status.AVAILABLE)
        return waitListRepository.save(waitlist)
    }

    fun expireAllOldWaitlist() {
        waitListRepository.updateExpiredByUpdateStatusAt()

    }
}
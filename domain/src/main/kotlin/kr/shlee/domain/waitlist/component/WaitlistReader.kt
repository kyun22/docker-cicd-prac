package kr.shlee.domain.waitlist.component

import kr.shlee.domain.common.error.WaitlistException
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistReader (
    val waitListRepository: WaitListRepository
){
    fun findByUserId(userId: String): Waitlist? {
        return waitListRepository.findByUserId(userId)
    }

    fun getByUserId(userId: String): Waitlist {
        return waitListRepository.findByUserId(userId)
            ?: throw WaitlistException(WaitlistException.WaitlistErrorResult.USER_NOT_FOUND)
    }

    fun getLastAvailableWaitlist(): Waitlist? {
        return waitListRepository.getLastAvailableWaitlist()

    }

    fun getAvailableCount(): Long {
        return waitListRepository.getAvailableCount()
    }

    fun findByToken(token: String): Waitlist? {
        return waitListRepository.findByToken(token)
    }

    fun findFirstWaitingWaitlist(): Waitlist? {
        return waitListRepository.findFirstWaitingWaitlist()
    }
}
package kr.shlee.domain.waitlist.component

import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistReader (
    val waitListRepository: WaitListRepository
){
    fun find(userId: String): Waitlist? {
        return waitListRepository.findByUserId(userId)
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
}
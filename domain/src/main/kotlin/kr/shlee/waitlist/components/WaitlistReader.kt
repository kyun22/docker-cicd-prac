package kr.shlee.waitlist.components

import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository
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
}
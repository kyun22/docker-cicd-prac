package kr.shlee.domain.waitlist.component

import kr.shlee.domain.common.error.WaitlistException
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistReader (
    val waitListRepository: WaitListRepository
){
    fun findAlreadyRegistered(userId: String): Waitlist? {
        waitListRepository.findByUserId(userId)
            ?: return null

        return waitListRepository.findByIdExceptExpired(userId)
    }

    fun getByUserId(userId: String): Waitlist {
        return waitListRepository.findByUserId(userId)
            ?: throw WaitlistException(WaitlistException.WaitlistErrorResult.USER_NOT_FOUND)
    }

    fun getAvailableWaitlistCount(): Long {
        return waitListRepository.getAvailableCount()
    }

    fun findFirstWaitingWaitlist(): Waitlist? {
        return waitListRepository.findFirstWaitingWaitlist()
    }

    fun getPosition(token: String): Long {
        val waitlist = (waitListRepository.findByToken(token)
            ?: throw WaitlistException(WaitlistException.WaitlistErrorResult.USER_NOT_FOUND))

        return waitListRepository.getLastAvailableWaitlist()
            ?.let { last -> waitlist.getPositionFromLastWaitlist(last) }
            ?: 0
    }

}
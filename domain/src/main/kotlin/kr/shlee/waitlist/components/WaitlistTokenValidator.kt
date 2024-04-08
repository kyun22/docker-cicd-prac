package kr.shlee.waitlist.components

import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistTokenValidator(
    val waitListRepository: WaitListRepository
) {
    fun validate(token: String): Boolean {
        waitListRepository.findByToken(token)?.let {
            return when (it.status) {
                Waitlist.Status.WAITING -> false
                Waitlist.Status.EXPIRED -> false
                Waitlist.Status.AVAILABLE -> true
            }
        } ?: return false
    }
}
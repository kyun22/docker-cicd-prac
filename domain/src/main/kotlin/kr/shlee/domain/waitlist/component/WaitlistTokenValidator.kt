package kr.shlee.domain.waitlist.component

import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistTokenValidator(
    val waitListRepository: WaitListRepository
) {
    fun validate(token: String): Boolean {
        val waitlist = waitListRepository.findByToken(token) ?: return false
        return when (waitlist.status) {
            Waitlist.Status.AVAILABLE -> true
            Waitlist.Status.WAITING -> false
            Waitlist.Status.EXPIRED -> false
        }
    }
}
package kr.shlee.domain.waitlist.component

import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistAppender(
    val waitListRepository: WaitListRepository
) {
    fun add(waitlist: Waitlist): Waitlist {
        return waitListRepository.save(waitlist)
    }
}
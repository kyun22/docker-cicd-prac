package kr.shlee.waitlist.component

import kr.shlee.waitlist.model.Waitlist
import kr.shlee.waitlist.repository.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistAppender(
    val waitListRepository: WaitListRepository
) {
    fun add(waitlist: Waitlist): Waitlist {
        return waitListRepository.save(waitlist)
    }
}
package kr.shlee.waitlist.components

import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository
import org.springframework.stereotype.Component

@Component
class WaitlistAppender(
    val waitListRepository: WaitListRepository
) {
    fun add(waitlist: Waitlist): Waitlist {
        return waitListRepository.save(waitlist)
    }
}
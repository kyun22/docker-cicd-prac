package kr.shlee.waitlist.infrastructure

import org.springframework.stereotype.Repository
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository

@Repository
class WaitlistCoreRepository(
    val waitlistJpaRepository: WaitlistJpaRepository
) : WaitListRepository {
    override fun findByUserIdAndEventId(userId: String, eventId: String): Waitlist {
        TODO("Not yet implemented")
    }

    override fun save(waitlist: Waitlist): Waitlist {
        return waitlistJpaRepository.save(waitlist)
    }

    override fun findById(id: String): Waitlist? {
        return waitlistJpaRepository.findById(id).orElse(null)
    }
}
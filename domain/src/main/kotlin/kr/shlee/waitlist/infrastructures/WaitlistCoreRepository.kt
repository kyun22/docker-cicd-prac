package kr.shlee.waitlist.infrastructures

import org.springframework.stereotype.Repository
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository

@Repository
class WaitlistCoreRepository(
    val waitlistJpaRepository: WaitlistJpaRepository
) : WaitListRepository {

    override fun save(waitlist: Waitlist): Waitlist {
        return waitlistJpaRepository.save(waitlist)
    }

    override fun findById(id: Long): Waitlist? {
        return waitlistJpaRepository.findById(id).orElse(null)
    }

    override fun findByUserId(userId: String): Waitlist? {
        return waitlistJpaRepository.findByUserId(userId).orElse(null)
    }

    override fun findByToken(token: String): Waitlist? {
        return waitlistJpaRepository.findByToken(token).orElse(null)
    }
}
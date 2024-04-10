package kr.shlee.waitlist.infrastructure

import org.springframework.stereotype.Repository
import kr.shlee.waitlist.model.Waitlist
import kr.shlee.waitlist.repository.WaitListRepository
import org.springframework.data.repository.findByIdOrNull

@Repository
class WaitlistCoreRepository(
    val waitlistJpaRepository: WaitlistJpaRepository,
    val waitlistCustomRepository: WaitlistCustomRepository
) : WaitListRepository {

    override fun save(waitlist: Waitlist): Waitlist {
        return waitlistJpaRepository.save(waitlist)
    }

    override fun findById(id: Long): Waitlist? {
        return waitlistJpaRepository.findByIdOrNull(id)
    }

    override fun findByUserId(userId: String): Waitlist? {
        return waitlistJpaRepository.findByUserId(userId).orElse(null)
    }

    override fun findByToken(token: String): Waitlist? {
        return waitlistJpaRepository.findByToken(token).orElse(null)
    }

    override fun getLastAvailableWaitlist(): Waitlist? {
        return waitlistCustomRepository.getLastAvailableWaitlist()
    }

    override fun getAvailableCount(): Long {
        return waitlistCustomRepository.getAvailableCount()
    }
}
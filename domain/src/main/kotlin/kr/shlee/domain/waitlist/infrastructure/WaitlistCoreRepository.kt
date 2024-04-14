package kr.shlee.domain.waitlist.infrastructure

import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

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

    override fun findFirstWaitingWaitlist(): Waitlist? {
        return waitlistCustomRepository.getFirstWaitingWaitlist()
    }

    override fun updateExpiredByUpdateStatusAt() {
        return waitlistCustomRepository.updateExpiredByUpdateStatusAt()
    }

    override fun findByIdExceptExpired(userId: String): Waitlist? {
        return waitlistCustomRepository.findByIdExceptExpired(userId)
    }
}
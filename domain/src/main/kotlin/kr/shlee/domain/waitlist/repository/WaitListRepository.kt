package kr.shlee.domain.waitlist.repository

import kr.shlee.domain.waitlist.model.Waitlist

interface WaitListRepository {
    fun save(waitlist: Waitlist): Waitlist
    fun findById(id: Long): Waitlist?
    fun findByUserId(userId: String): Waitlist?
    fun findByToken(token: String): Waitlist?
    fun getLastAvailableWaitlist(): Waitlist?
    fun getAvailableCount(): Long
    fun findFirstWaitingWaitlist(): Waitlist?
    fun updateExpiredByUpdateStatusAt()
}


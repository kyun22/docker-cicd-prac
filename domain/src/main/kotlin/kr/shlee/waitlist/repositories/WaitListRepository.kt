package kr.shlee.waitlist.repositories

import kr.shlee.waitlist.models.Waitlist

interface WaitListRepository {
    fun save(waitlist: Waitlist): Waitlist
    fun findById(id:Long): Waitlist?
    fun findByUserId(userId: String): Waitlist?
    fun findByToken(token: String): Waitlist?
    fun getLastAvailableWaitlist(): Waitlist?
    fun getAvailableCount(): Long
}


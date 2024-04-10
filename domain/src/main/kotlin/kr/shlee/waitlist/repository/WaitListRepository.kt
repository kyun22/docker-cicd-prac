package kr.shlee.waitlist.repository

import kr.shlee.waitlist.model.Waitlist

interface WaitListRepository {
    fun save(waitlist: Waitlist): Waitlist
    fun findById(id:Long): Waitlist?
    fun findByUserId(userId: String): Waitlist?
    fun findByToken(token: String): Waitlist?
    fun getLastAvailableWaitlist(): Waitlist?
    fun getAvailableCount(): Long
}


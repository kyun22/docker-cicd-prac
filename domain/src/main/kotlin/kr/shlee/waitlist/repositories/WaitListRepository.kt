package kr.shlee.waitlist.repositories

import kr.shlee.waitlist.models.Waitlist

interface WaitListRepository {
    fun findByUserIdAndEventId(userId: String, eventId: String): Waitlist?
    fun save(waitlist: Waitlist): Waitlist
    fun findById(id:String): Waitlist?
}


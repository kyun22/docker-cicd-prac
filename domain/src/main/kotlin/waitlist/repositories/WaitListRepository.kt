package waitlist.repositories

import waitlist.models.Waitlist

interface WaitListRepository {
    fun findByUserIdAndEventId(userId: String, eventId: String): Waitlist?
    fun save(waitlist: Waitlist): Waitlist
}


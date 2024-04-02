package waitlist.repositories

import waitlist.models.Event

interface EventRepository {
    abstract fun findById(eventId: String): Event?
}
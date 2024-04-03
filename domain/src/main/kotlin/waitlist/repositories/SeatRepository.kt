package waitlist.repositories

import waitlist.models.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?

}
package kr.shlee.waitlist.repositories

import kr.shlee.waitlist.models.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?

}
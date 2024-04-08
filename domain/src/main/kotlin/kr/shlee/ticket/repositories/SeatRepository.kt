package kr.shlee.ticket.repositories

import kr.shlee.ticket.models.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?

}
package kr.shlee.ticket.repository

import kr.shlee.ticket.model.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?

}
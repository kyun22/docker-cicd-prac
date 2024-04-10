package kr.shlee.domain.ticket.repository

import kr.shlee.domain.ticket.model.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?

}
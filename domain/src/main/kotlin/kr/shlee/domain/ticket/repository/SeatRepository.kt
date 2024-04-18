package kr.shlee.domain.ticket.repository

import kr.shlee.domain.ticket.model.Seat

interface SeatRepository {
    fun findById(id: Long) : Seat?
    fun findSeats(seatIds: List<String>) : List<Seat>
    fun findAllByEventId(eventId: String) : List<Seat>
    fun save(seat: Seat) : Seat
}
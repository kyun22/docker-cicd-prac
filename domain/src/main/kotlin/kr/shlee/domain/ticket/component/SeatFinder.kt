package kr.shlee.domain.ticket.component

import kr.shlee.domain.common.error.SeatException
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.repository.SeatRepository
import org.springframework.stereotype.Component

@Component
class SeatFinder(
    private val seatRepository: SeatRepository
) {
    fun findSeats(seatIds: List<String>): List<Seat> {
        val seats = seatRepository.findSeats(seatIds)
        if(!checkAllAvailable(seats)) throw SeatException(SeatException.SeatErrorResult.NOT_AVAILABLE_SEATS)
        return seats
    }

    private fun checkAllAvailable(seats: List<Seat>) =
        seats.all { seat -> seat.status == Seat.Status.AVAILABLE }

    fun findSeatsByEventId(eventId: String): List<Seat> {
        return seatRepository.findAllByEventId(eventId)
    }
}
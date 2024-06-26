package kr.shlee.api.event.dto

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.ticket.model.Seat
import java.time.format.DateTimeFormatter

class EventListResponse(
    val events: List<EventResponse>
) {
    companion object {
        fun of(events: List<Event>): MutableList<EventResponse> {
            val list = mutableListOf<EventResponse>()
            events.map { event: Event ->
                list.add(
                    EventResponse.of(event, emptyList())
                )
            }
            return list
        }
    }
}

data class EventResponse(
    val id: String,
    val name: String,
    val location: String,
    val availableSeats: Int,
    val seats: List<SeatVo>,
    val date: String
) {
    companion object {
        fun convertSeatsToVo(seats: List<Seat>): List<SeatVo> {
            return seats.map { seat: Seat -> SeatVo.of(seat) }
        }

        fun of(event: Event, seats: List<Seat>): EventResponse {
            return EventResponse(
                id = event.id,
                name = event.concert.name,
                location = event.location,
                availableSeats = seats.filter { seat -> seat.status == Seat.Status.AVAILABLE }.size,
                seats = convertSeatsToVo(seats),
                date = event.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
        }
    }
}


data class SeatVo(val id: String, val number: String, val price: Int) {
    companion object {
        fun of(seat: Seat): SeatVo {
            return SeatVo(seat.id, seat.number, seat.price)
        }
    }
}

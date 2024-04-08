package kr.shlee.event.dto

import kr.shlee.waitlist.models.Event
import kr.shlee.waitlist.models.Seat
import java.time.format.DateTimeFormatter

class EventListResponse(
    val events: List<EventResponse>
) {
    companion object {
        fun newOf(events: List<Event>): MutableList<EventResponse> {
            val list = mutableListOf<EventResponse>()
            events.map { event: Event ->
                list.add(
                    EventResponse(
                        id = event.id,
                        name = event.name,
                        localtion = event.location,
                        availableSeats = event.getAvailableSeatCount(),
                        seats = EventResponse.convertSeatsToVo(event.seats),
                        date = event.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )
                )
            }
            return list
        }
    }
}

data class EventResponse(
    val id: String,
    val name: String,
    val localtion: String,
    val availableSeats: Int,
    val seats: List<SeatVo>,
    val date: String
) {
    companion object {
        fun convertSeatsToVo(seats: MutableList<Seat>): List<SeatVo> {
            return seats.map { seat: Seat -> SeatVo.of(seat) }
        }
    }
}


data class SeatVo(val id: Long, val number: String, val price: Int) {
    companion object {
        fun of(seat: Seat): SeatVo {
            return SeatVo(seat.id, seat.number, seat.price)
        }
    }
}

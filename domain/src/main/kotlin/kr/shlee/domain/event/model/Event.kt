package kr.shlee.domain.event.model

import jakarta.persistence.*
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    val location: String,
    val date: LocalDate,

    @ManyToOne
    val concert: Concert,

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val seats: MutableList<Seat> = mutableListOf()
) {

    fun getAvailableSeatCount(): Int {
        return seats.filter { seat -> seat.status == Seat.Status.AVAILABLE }.size
    }

    fun addSeat(seat: Seat) {
        seats.add(seat)
    }
}
package kr.shlee.domain.ticket.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    val location: String,
    val date: LocalDateTime,

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
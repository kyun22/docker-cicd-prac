package kr.shlee.domain.ticket.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import kr.shlee.domain.point.model.User

@Entity
class Ticket(
    @Id @GeneratedValue
    val id: String?,
    @OneToOne
    val user: User,
    @OneToOne(cascade = [CascadeType.PERSIST])
    val seat: Seat,
    var status: Status
) {
    enum class Status {
        WAITING_PAYMENT, COMPLETE_PAYMENT, NONE
    }

    fun updateStatus(after: Status) {
        when (after) {
            Status.COMPLETE_PAYMENT -> seat.status = Seat.Status.PURCHASED
            Status.WAITING_PAYMENT -> seat.status = Seat.Status.RESERVED
            Status.NONE -> seat.status = Seat.Status.AVAILABLE
        }
    }

    companion object {
        fun makeTickets(user: User, seats: List<Seat>): List<Ticket> {
            return seats.map { seat -> Ticket(null, user, seat, Status.WAITING_PAYMENT) }
        }
    }

}

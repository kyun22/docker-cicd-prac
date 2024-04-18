package kr.shlee.domain.ticket.model

import jakarta.persistence.*
import kr.shlee.domain.common.base.BaseEntity
import kr.shlee.domain.point.model.User

@Entity
class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "seat_id")
    val seat: Seat,
    var status: Status
) : BaseEntity() {
    enum class Status {
        WAITING_PAYMENT, COMPLETE_PAYMENT, EXPIRED
    }

    companion object {
        fun makeTickets(user: User, seats: List<Seat>): List<Ticket> {
            return seats.map { seat -> Ticket(null, user, seat, Status.WAITING_PAYMENT) }
        }
    }

    fun completePayment() {
        status = Status.COMPLETE_PAYMENT
        seat.status = Seat.Status.PURCHASED
        user.subtractPoint(seat.price)
    }

    fun expireAndRefreshSeat() {
        status = Status.EXPIRED
        seat.refreshStatus()
    }

}

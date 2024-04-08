package kr.shlee.waitlist.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class Ticket(
    @Id @GeneratedValue
    val id: String?,
    val userId: String,
    @OneToOne(cascade = [CascadeType.PERSIST])
    val seat: Seat,
    var status: Status
) {
    enum class Status{
        WAITING_PAYMENT, COMPLETE_PAYMENT, NONE
    }

    fun updateStatus(after: Status) {
        when (after) {
            Status.COMPLETE_PAYMENT -> seat.status = Seat.Status.PURCHASED
            Status.WAITING_PAYMENT -> seat.status = Seat.Status.RESERVED
            Status.NONE -> seat.status = Seat.Status.AVAILABLE
        }
    }

    fun refreshStatus() {
        when (seat.status) {
            Seat.Status.RESERVED -> status = Status.WAITING_PAYMENT
            Seat.Status.PURCHASED -> status = Status.COMPLETE_PAYMENT
            Seat.Status.AVAILABLE -> status = Status.NONE
        }
    }
}

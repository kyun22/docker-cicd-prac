package kr.shlee.domain.ticket.model

import jakarta.persistence.*
import kr.shlee.domain.event.model.Event
import java.time.LocalDateTime

@Entity
class Seat (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event: Event,
    val number: String,
    val price: Int,
    var reservedAt: LocalDateTime?,
    var status: Status
){
    enum class Status {
        AVAILABLE, RESERVED, PURCHASED
    }

    fun changeToReserved() {
        status = Status.RESERVED
        reservedAt = LocalDateTime.now()
    }

    fun refreshStatus() {
        status = Status.AVAILABLE
        reservedAt = null
    }
}



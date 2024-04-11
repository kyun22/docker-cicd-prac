package kr.shlee.domain.ticket.model

import jakarta.persistence.*
import kr.shlee.domain.event.model.Event

@Entity
class Seat (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event: Event,
    val number: String,
    val price: Int,
    var status: Status
){
    enum class Status {
        AVAILABLE, RESERVED, PURCHASED
    }
}



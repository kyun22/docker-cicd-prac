package waitlist.models

import jakarta.persistence.*

@Entity
class Seat (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

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



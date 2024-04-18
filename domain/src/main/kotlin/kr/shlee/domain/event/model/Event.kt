package kr.shlee.domain.event.model

import jakarta.persistence.*
import kr.shlee.domain.ticket.model.Concert
import java.time.LocalDate

@Entity
class Event(
    @Id
    val id: String,
    val location: String,
    val date: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "concert_id")
    val concert: Concert,
)
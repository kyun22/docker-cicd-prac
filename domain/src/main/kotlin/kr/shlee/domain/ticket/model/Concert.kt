package kr.shlee.domain.ticket.model

import jakarta.persistence.*
import kr.shlee.domain.event.model.Event

@Entity
class Concert(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    val name: String,
    val singer: String,
) {
}
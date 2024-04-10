package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaRepository : JpaRepository<Seat, Long> {
}
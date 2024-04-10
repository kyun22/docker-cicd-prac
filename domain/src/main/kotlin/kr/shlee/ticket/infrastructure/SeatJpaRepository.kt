package kr.shlee.ticket.infrastructure

import kr.shlee.ticket.model.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaRepository : JpaRepository<Seat, Long> {
}
package kr.shlee.ticket.infrastructures

import kr.shlee.ticket.models.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaRepository : JpaRepository<Seat, Long> {
}
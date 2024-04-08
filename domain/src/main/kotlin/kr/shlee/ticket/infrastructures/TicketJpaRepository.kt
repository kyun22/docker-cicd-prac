package kr.shlee.ticket.infrastructures

import kr.shlee.ticket.models.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaRepository : JpaRepository<Ticket, String> {
}
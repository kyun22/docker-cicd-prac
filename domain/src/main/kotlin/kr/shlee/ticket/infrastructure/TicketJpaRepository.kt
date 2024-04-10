package kr.shlee.ticket.infrastructure

import kr.shlee.ticket.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaRepository : JpaRepository<Ticket, String> {
}
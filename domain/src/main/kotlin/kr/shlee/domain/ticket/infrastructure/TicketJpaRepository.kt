package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaRepository : JpaRepository<Ticket, String> {
}
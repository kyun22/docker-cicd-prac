package kr.shlee.ticket.infrastructure

import kr.shlee.ticket.model.Ticket
import kr.shlee.ticket.repository.TicketRepository
import org.springframework.stereotype.Repository

@Repository
class TicketCoreRepository (
    val ticketJpaRepository: TicketJpaRepository
) : TicketRepository {
    override fun save(ticket: Ticket): Ticket {
        TODO("Not yet implemented")
    }

    override fun findAllById(ticketIds: List<String>): List<Ticket> {
        TODO("Not yet implemented")
    }
}
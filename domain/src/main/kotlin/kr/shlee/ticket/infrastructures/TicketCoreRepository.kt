package kr.shlee.ticket.infrastructures

import kr.shlee.ticket.models.Ticket
import kr.shlee.ticket.repositories.TicketRepository
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
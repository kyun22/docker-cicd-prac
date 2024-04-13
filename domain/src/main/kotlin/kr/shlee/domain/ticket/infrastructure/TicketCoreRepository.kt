package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.TicketRepository
import org.springframework.stereotype.Repository

@Repository
class TicketCoreRepository (
    private val ticketJpaRepository: TicketJpaRepository,
    private val ticketCustomRepository: TicketCustomRepository
) : TicketRepository {
    override fun save(ticket: Ticket): Ticket {
        return ticketJpaRepository.save(ticket)
    }

    override fun findAllByIds(ticketIds: List<String>): List<Ticket>? {
        return ticketCustomRepository.findAllByIds(ticketIds)
    }

    override fun findAllReservedAndNotPaidTickets(): List<Ticket>? {
        return ticketCustomRepository.findAllReservedAndNotPaidTickets()
    }

}
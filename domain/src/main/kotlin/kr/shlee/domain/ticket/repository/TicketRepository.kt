package kr.shlee.domain.ticket.repository

import kr.shlee.domain.ticket.model.Ticket

interface TicketRepository {
    fun save(ticket: Ticket) : Ticket
    fun findAllById(ticketIds: List<String>) : List<Ticket>

}
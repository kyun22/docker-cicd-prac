package kr.shlee.ticket.repositories

import kr.shlee.ticket.models.Ticket

interface TicketRepository {
    fun save(ticket: Ticket) : Ticket
    fun findAllById(ticketIds: List<String>) : List<Ticket>

}
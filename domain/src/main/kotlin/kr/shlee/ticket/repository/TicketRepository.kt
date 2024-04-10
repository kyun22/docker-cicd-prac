package kr.shlee.ticket.repository

import kr.shlee.ticket.model.Ticket

interface TicketRepository {
    fun save(ticket: Ticket) : Ticket
    fun findAllById(ticketIds: List<String>) : List<Ticket>

}
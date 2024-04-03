package waitlist.repositories

import waitlist.models.Ticket

interface TicketRepository {
    fun save(ticket: Ticket) : Ticket
    fun findAllById(ticketIds: List<String>) : List<Ticket>

}
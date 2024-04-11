package kr.shlee.domain.ticket.component

import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.TicketRepository
import org.springframework.stereotype.Component

@Component
class TicketManager(
    private val ticketRepository: TicketRepository
) {
    fun reserve(user: User, seats: List<Seat>): List<Ticket> {
        // ticket을 생성한다.
        val tickets = Ticket.makeTickets(user, seats)
        tickets.forEach { ticket -> ticketRepository.save(ticket) }
        return tickets
    }
}
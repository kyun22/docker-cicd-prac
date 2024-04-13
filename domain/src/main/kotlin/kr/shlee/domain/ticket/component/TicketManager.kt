package kr.shlee.domain.ticket.component

import kr.shlee.domain.common.error.TicketException
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.TicketRepository
import org.springframework.stereotype.Component

@Component
class TicketManager(
    private val ticketRepository: TicketRepository,
) {
    fun reserve(user: User, seats: List<Seat>): List<Ticket> {
        // ticket을 생성한다.
        val tickets = Ticket.makeTickets(user, seats)
        tickets.forEach { ticket ->
            ticket.seat.changeToReserved()
            ticketRepository.save(ticket)
        }
        return tickets
    }

    fun pay(user: User, ticketIds: List<String>): List<Ticket> {
        val tickets = ticketRepository.findAllByIds(ticketIds)
            ?: throw TicketException(TicketException.TicketErrorResult.TICKET_NOT_FOUND)

        if (user.id != tickets.first().user.id)
            throw TicketException(TicketException.TicketErrorResult.NOT_TICKET_OWNER)

        // user의 포인트가 충분한지 체크
        if (!user.isEnoughPoint(getTotalPrice(tickets)))
            throw TicketException(TicketException.TicketErrorResult.USER_POINT_NOT_ENOUGH)

        // 티켓 상태 업데이트 : 완료
        tickets.forEach { ticket -> ticket.completePayment() }
        return tickets
    }

    fun findAllExpireTarget(): List<Ticket>? {
        return ticketRepository.findAllReservedAndNotPaidTickets()
    }

    fun expire(ticket: Ticket): Ticket {
        ticket.expireAndRefreshSeat()
        return ticketRepository.save(ticket)
    }

    private fun getTotalPrice(tickets: List<Ticket>) =
        tickets.sumOf { ticket -> ticket.seat.price }
}
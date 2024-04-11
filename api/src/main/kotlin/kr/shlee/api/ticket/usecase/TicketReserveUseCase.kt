package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.dto.TicketResponse
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.ticket.component.SeatFinder
import kr.shlee.domain.ticket.component.TicketManager
import kr.shlee.domain.ticket.model.Ticket
import org.springframework.stereotype.Component

@Component
class TicketReserveUseCase (
    val userManager: UserManager,
    val seatFinder: SeatFinder,
    val ticketManager: TicketManager
){
    //todo, transactional
    fun execute(request: TicketRequest.Reserve): TicketResponse.Reserve {
        // user를 가져온다.
        val user = userManager.find(request.userId)

        // seats를 가져온다.
        val seats = seatFinder.findSeats(request.seatIds)

        // ticket을 생성하고 저장(예약)
        return TicketResponse.Reserve.of(ticketManager.reserve(user, seats))
    }

}


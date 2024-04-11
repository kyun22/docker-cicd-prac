package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.dto.TicketResponse
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.ticket.component.TicketManager
import org.springframework.stereotype.Component

@Component
class TicketPaymentUseCase(
    val userManager: UserManager,
    val ticketManager: TicketManager
) {

    // todo, transactional 처리
    fun execute(request: TicketRequest.Payment): TicketResponse.Payment {
        // user를 가져온다.
        val user = userManager.find(request.userId)

        // tickets 결재 진행
        val tickets = ticketManager.pay(user, request.ticketIds)

        return TicketResponse.Payment.of(tickets)
    }

}

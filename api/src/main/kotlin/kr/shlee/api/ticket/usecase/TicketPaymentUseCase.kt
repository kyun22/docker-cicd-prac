package kr.shlee.api.ticket.usecase

import kr.shlee.api.common.application_event.CustomApplicationEventPublisher
import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.dto.TicketResponse
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.ticket.component.TicketManager
import kr.shlee.domain.ticket.model.PaidEvent
import org.springframework.stereotype.Component

@Component
class TicketPaymentUseCase(
    val userManager: UserManager,
    val ticketManager: TicketManager,
    val applicationEventPublisher: CustomApplicationEventPublisher
) {

    // todo, transactional 처리
    fun execute(request: TicketRequest.Payment): TicketResponse.Payment {
        // user를 가져온다.
        val user = userManager.get(request.userId)

        // tickets 결제 진행
        val tickets = ticketManager.pay(user, request.ticketIds)

        // 결제 완료 이벤트 발행
        applicationEventPublisher.publishEvent(PaidEvent(user.id))
        return TicketResponse.Payment.of(tickets)
    }

}

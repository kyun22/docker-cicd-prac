package kr.shlee.api.ticket.usecase

import kr.shlee.domain.ticket.component.TicketManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TicketScheduleExpireUseCase (
    private val ticketManager: TicketManager
){
    @Scheduled(fixedDelay = 60000)
    fun execute() {
        // 예약 후 5분 이상 지난 티켓들을 가져온다.
        val tickets = ticketManager.findAllExpireTarget() ?: return

        // 티켓을 만료시키고, Seat는 예약상태를 해제한다.
        tickets.forEach { ticket -> ticketManager.expire(ticket) }
    }

}
package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.dto.TicketResponse
import kr.shlee.domain.common.error.TicketException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.ticket.component.SeatFinder
import kr.shlee.domain.ticket.component.TicketManager
import org.springframework.dao.CannotAcquireLockException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Component
class TicketReserveUseCase(
    val userManager: UserManager,
    val seatFinder: SeatFinder,
    val ticketManager: TicketManager
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    operator fun invoke(request: TicketRequest.Reserve): TicketResponse.Reserve {
        // user를 가져온다.
        val user = userManager.getWithLock(request.userId)

        // seats를 가져온다.
        val seats = seatFinder.findSeats(request.seatIds)

        // ticket을 생성하고 저장(예약)
        return TicketResponse.Reserve.of(ticketManager.reserve(user, seats))
    }

}


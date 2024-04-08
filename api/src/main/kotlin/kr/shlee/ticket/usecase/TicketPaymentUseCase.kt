package kr.shlee.ticket.usecase

import org.springframework.stereotype.Component
import kr.shlee.ticket.dto.TicketRequest
import kr.shlee.ticket.dto.TicketResponse
import kr.shlee.waitlist.models.Ticket
import kr.shlee.waitlist.repositories.TicketRepository
import kr.shlee.waitlist.repositories.UserRepository

@Component
class TicketPaymentUseCase(
    val userRepository: UserRepository,
    val ticketRepository: TicketRepository
) {

    // todo, transactional 처리
    fun execute(request: TicketRequest.Payment): TicketResponse.Payment {
        // todo, 토큰 검증

        // 사용자를 가져온다.
        val user = userRepository.findById(request.userId) ?: throw RuntimeException("존재하지 않는 유저")

        // tickets 가져온다.
        val tickets = ticketRepository.findAllById(request.ticketIds)

        // user의 포인트가 충분한지 체크
        val totalPrice = tickets.sumOf { it.seat.price }
        if (user.point < totalPrice) throw RuntimeException("포인트가 부족합니다.")

        // totalPrice 만큼 포인트 차감하고 유저 저장 + 티켓 저장 (하나의 트랜잭션)
        // ticket 은 상태를 바꿔서 저장, seat의 상태도 함께 바꾼다 (cascade)
        val savedUser = userRepository.save(user.apply { this.subtractPoint(totalPrice) })
        val results =
            tickets.map { ticket -> ticketRepository.save(ticket.apply { updateStatus(Ticket.Status.COMPLETE_PAYMENT) }) }
        return TicketResponse.Payment.of(results, savedUser.point)
    }

}

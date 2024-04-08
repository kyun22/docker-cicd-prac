package kr.shlee.ticket.usecase

import org.springframework.stereotype.Component
import kr.shlee.ticket.dto.TicketRequest
import kr.shlee.ticket.dto.TicketResponse
import kr.shlee.ticket.models.Seat
import kr.shlee.ticket.models.Ticket
import kr.shlee.ticket.repositories.SeatRepository
import kr.shlee.ticket.repositories.TicketRepository
import kr.shlee.waitlist.repositories.UserRepository

@Component
class TicketReserveUseCase (
    val userRepository: UserRepository,
    val seatRepository: SeatRepository,
    val ticketRepository: TicketRepository
){
    fun execute(request: TicketRequest.Reserve): TicketResponse.Reserve {
        // todo, 토큰 검증

        // user를 가져온다.
        val user = userRepository.findById(request.userId) ?: throw RuntimeException("존재하지 않는 유저")

        // seats를 가져온다.
        val seats = mutableListOf<Seat>()
        for (id in request.seatIds) {
            val seat = seatRepository.findById(id) ?: throw RuntimeException("존재하지 않은 좌석")
            seats.add(seat)
        }

        // ticket을 생성한다.
        val tickets = seats.map { Ticket(null, request.userId, it, Ticket.Status.WAITING_PAYMENT) }

        // todo, transaction 처리
        // ticket을 저장(status:WAIT_PAYMENT)한다.
        tickets.forEach { ticket -> ticketRepository.save(ticket) }
        return TicketResponse.Reserve.of(tickets)
    }

}


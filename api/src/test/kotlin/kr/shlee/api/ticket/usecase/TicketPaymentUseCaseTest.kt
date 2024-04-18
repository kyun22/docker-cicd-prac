package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.domain.common.error.TicketException
import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.SeatRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class TicketPaymentUseCaseTest {

    @Autowired
    lateinit var ticketReserveUseCase: TicketReserveUseCase

    @Autowired
    lateinit var ticketPaymentUseCase: TicketPaymentUseCase

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var userManager: UserManager

    @Test
    fun `티켓 결제 실패 - 존재하지 않는 유저 `(){
        //given
        val request = TicketRequest.Payment("user1", listOf("ticket1", "ticket2", "ticket3"))

        //when
        val throws = org.junit.jupiter.api.assertThrows<UserException> { ticketPaymentUseCase(request) }

        //then
        Assertions.assertThat(throws.errorResult).isEqualTo(UserException.UserErrorResult.USER_NOT_FOUND)
    }


    @Test
    fun `티켓 결제 실패 - 존재하지 않는 티켓 `(){
        //given
        val user = userManager.save(User("user1", 0))
        val request = TicketRequest.Payment("user1", listOf("ticket1", "ticket2", "ticket3"))

        //when
        val throws = org.junit.jupiter.api.assertThrows<TicketException> { ticketPaymentUseCase(request) }

        //then
        Assertions.assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.TICKET_NOT_FOUND)
    }

    @Test
    fun `티켓 결제 실패 - 티켓을 예약한 유저와 다른 유저의 요청 `(){
        //given
        val user1 = userManager.save(User("user1", 10000))
        val user2 = userManager.save(User("user2", 0))
        val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 1000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 2000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 3000, null, Seat.Status.AVAILABLE))
        val seatIds = listOf(seat1.id, seat2.id, seat3.id)
        val reserveRequest = TicketRequest.Reserve(user1.id, event.id, seatIds)
        val reserveResponse = ticketReserveUseCase(reserveRequest)
        val ticketIds = reserveResponse.tickets.map { ticket -> ticket.id }
        val paymentRequest = TicketRequest.Payment(user2.id, ticketIds)

        //when
        val throws = org.junit.jupiter.api.assertThrows<TicketException> { ticketPaymentUseCase(paymentRequest) }

        //then
        Assertions.assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.NOT_TICKET_OWNER)
    }

    @Test
    fun `티켓 결제 실패 - 유저의 포인트가 부족함 `(){
        //given
        val user1 = userManager.save(User("user1", 0))
        val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 1000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 2000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 3000, null, Seat.Status.AVAILABLE))
        val seatIds = listOf(seat1.id, seat2.id, seat3.id)
        val reserveRequest = TicketRequest.Reserve(user1.id, event.id, seatIds)
        val reserveResponse = ticketReserveUseCase(reserveRequest)
        val ticketIds = reserveResponse.tickets.map { ticket -> ticket.id }
        val paymentRequest = TicketRequest.Payment(user1.id, ticketIds)

        //when
        val throws = org.junit.jupiter.api.assertThrows<TicketException> { ticketPaymentUseCase(paymentRequest) }

        //then
        Assertions.assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.USER_POINT_NOT_ENOUGH)
    }

    @Test
    fun `티켓 결제 성공`(){
        @Test
        fun `티켓 결제 실패 - 티켓을 예약한 유저와 다른 유저의 요청 `(){
            //given
            val user1 = userManager.save(User("user1", 10000))
            val user2 = userManager.save(User("user2", 0))
            val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
            val seat1 = seatRepository.save(Seat("seat1", event, "1", 1000, null, Seat.Status.AVAILABLE))
            val seat2 = seatRepository.save(Seat("seat2", event, "2", 2000, null, Seat.Status.AVAILABLE))
            val seat3 = seatRepository.save(Seat("seat3", event, "3", 3000, null, Seat.Status.AVAILABLE))
            val seatIds = listOf(seat1.id, seat2.id, seat3.id)
            val reserveRequest = TicketRequest.Reserve(user1.id, event.id, seatIds)
            val reserveResponse = ticketReserveUseCase(reserveRequest)
            val ticketIds = reserveResponse.tickets.map { ticket -> ticket.id }
            val paymentRequest = TicketRequest.Payment(user2.id, ticketIds)

            //when
            val paymentResponse = ticketPaymentUseCase(paymentRequest)

            //then
            Assertions.assertThat(paymentResponse.tickets.all { ticket -> ticket.status == Ticket.Status.COMPLETE_PAYMENT })
            Assertions.assertThat(paymentResponse.userId).isEqualTo(user1.id)
            Assertions.assertThat(paymentResponse.point).isEqualTo(user1.point - seat1.price - seat2.price - seat3.price)

        }
    }
}
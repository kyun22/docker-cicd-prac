package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.domain.common.error.SeatException
import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.SeatRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class TicketReserveUseCaseTest {

    @Autowired
    lateinit var ticketReserveUseCase: TicketReserveUseCase

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var userManager: UserManager

    @Test
    fun `티켓 예약 성공`() {
        //given
        val user = userManager.save(User("user1", 0))
        val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 10000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 20000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 30000, null, Seat.Status.AVAILABLE))
        val seatIds = listOf(seat1.id, seat2.id, seat3.id)
        val request = TicketRequest.Reserve(user.id, event.id, seatIds)

        //when
        val response = ticketReserveUseCase(request)

        //then
        assertThat(response.tickets.size).isEqualTo(seatIds.size)
        assertThat(response.totalPrice).isEqualTo(seat1.price + seat2.price + seat3.price)
        assertThat(response.quantity).isEqualTo(seatIds.size)
        assertThat(response.status).isEqualTo(Ticket.Status.WAITING_PAYMENT)
    }

    @Test
    fun `티켓 예약 실패 - 선택된 좌석 중 하나라도 AVAILABLE 하지 않으면`(){
        val user = userManager.save(User("user1", 0))
        val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 10000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 20000, null, Seat.Status.RESERVED))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 30000, null, Seat.Status.AVAILABLE))
        val seatIds = listOf(seat1.id, seat2.id, seat3.id)
        val request = TicketRequest.Reserve(user.id, event.id, seatIds)

        //when
        val throws = assertThrows<SeatException> { ticketReserveUseCase(request) }
        assertThat(throws.errorResult).isEqualTo(SeatException.SeatErrorResult.NOT_AVAILABLE_SEATS)
    }

    @Test
    fun `티켓 예약 실패 - 유저가 존재하지 않으면`(){
        val user = userManager.save(User("user1", 0))
        val event = eventRepository.save(Event("event1", "location", LocalDate.now(), Concert("concert1", "콘서트", "아이유")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 10000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 20000, null, Seat.Status.RESERVED))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 30000, null, Seat.Status.AVAILABLE))
        val seatIds = listOf(seat1.id, seat2.id, seat3.id)
        val request = TicketRequest.Reserve("user2", event.id, seatIds)

        //when
        val throws = assertThrows<UserException> { ticketReserveUseCase(request) }
        assertThat(throws.errorResult).isEqualTo(UserException.UserErrorResult.USER_NOT_FOUND)
    }


}
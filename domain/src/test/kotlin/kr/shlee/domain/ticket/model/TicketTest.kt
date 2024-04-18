package kr.shlee.domain.ticket.model

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import kotlin.test.Test

class TicketTest {
    @Test
    fun `Seats를 받아 Ticket 생성`() {
        // given
        val seats = mutableListOf<Seat>()
        val user = User("user1", 0)
        for (i in 1..10) {
            seats.add(
                Seat(
                    "s1",
                    Event("event1", "loc", LocalDate.now(), Concert("con1", "name", "singer")),
                    i.toString(),
                    10000,
                    null,
                    Seat.Status.AVAILABLE
                )
            )
        }

        //when
        val tickets = Ticket.makeTickets(user, seats.toList())

        //then
        assertThat(tickets.size).isEqualTo(10)
        assertThat(tickets.first().user.id).isEqualTo(tickets.last().user.id).isEqualTo("user1")
    }

    @Test
    fun `결제 처리 테스트`() {
        // given
        val user = User("user1", 10000)
        val seat = Seat("s1", Event("event1", "loc", LocalDate.now(), Concert("con1", "name", "singer")), "1", 10000, null, Seat.Status.RESERVED)
        val ticket = Ticket.makeTickets(user, listOf(seat)).first()
        assertThat(ticket.status).isEqualTo(Ticket.Status.WAITING_PAYMENT)
        assertThat(ticket.seat.status).isEqualTo(Seat.Status.RESERVED)
        assertThat(ticket.user.point).isEqualTo(10000)

        // when
        ticket.completePayment()

        // then
        assertThat(ticket.status).isEqualTo(Ticket.Status.COMPLETE_PAYMENT)
        assertThat(ticket.seat.status).isEqualTo(Seat.Status.PURCHASED)
        assertThat(ticket.user.point).isEqualTo(0)
    }

    @Test
    fun `티켓 만료 테스트 - 티켓을 만료하고 좌석을 refresh 한다`(){
        //given
        val seat = Seat("seat1", Event("event1", "loca", LocalDate.now(), Concert("concert1", "콘서트", "아이유")), "1", 10000, null, Seat.Status.RESERVED)
        val ticket = Ticket("ticket1", User("user1", 0), seat, Ticket.Status.WAITING_PAYMENT)

        //when
        ticket.expireAndRefreshSeat()

        //then
        assertThat(ticket.status).isEqualTo(Ticket.Status.EXPIRED)
        assertThat(ticket.seat.status).isEqualTo(Seat.Status.AVAILABLE)
        assertThat(ticket.seat.reservedAt).isNull()
    }



}
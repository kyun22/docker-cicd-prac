package kr.shlee.api.ticket.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.component.TicketManager
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.waitlist.component.WaitlistWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class TicketScheduleExpireUseCaseTest {
    @MockK
    lateinit var ticketManager: TicketManager

    @InjectMockKs
    lateinit var ticketScheduleExpireUseCase: TicketScheduleExpireUseCase

    @Test
    fun `예약 후 5분이 지난 티켓은 만료시킨다`() {
        //given
        @SpyK
        val seat1 = Seat("seat1", Event("event1", "loca", LocalDate.now(), Concert("concert1", "콘서트", "아이유"), mutableListOf<Seat>()), "1", 10000, null, Seat.Status.RESERVED)
        @SpyK
        val seat2 = Seat("seat2", Event("event1", "loca", LocalDate.now(), Concert("concert1", "콘서트", "아이유"), mutableListOf<Seat>()), "2", 10000, null, Seat.Status.RESERVED)
        @SpyK
        val ticket1 = Ticket("ticket1", User("user1", 0), seat1, Ticket.Status.WAITING_PAYMENT)
        @SpyK
        val ticket2 = Ticket("ticket2", User("user2", 0), seat2, Ticket.Status.WAITING_PAYMENT)
        every { ticketManager.findAllExpireTarget() } returns listOf(ticket1, ticket2)
        every { ticketManager.expire(ticket1) } returns ticket1.apply { expireAndRefreshSeat() }
        every { ticketManager.expire(ticket2) } returns ticket2.apply { expireAndRefreshSeat() }

        //when
        ticketScheduleExpireUseCase.execute()

        //then
        assertThat(ticket1.status).isEqualTo(Ticket.Status.EXPIRED)
        assertThat(ticket2.status).isEqualTo(Ticket.Status.EXPIRED)
        assertThat(ticket1.seat.status).isEqualTo(Seat.Status.AVAILABLE)
        assertThat(ticket2.seat.status).isEqualTo(Seat.Status.AVAILABLE)
    }

}
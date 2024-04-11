package kr.shlee.domain.ticket.component

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.common.error.TicketException
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.TicketRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class TicketManagerTest {
    @MockK
    lateinit var ticketRepository: TicketRepository

    @InjectMockKs
    lateinit var ticketManager: TicketManager

    @Test
    fun `예약 처리 테스트`(){
        //given
        val seats = mutableListOf<Seat>()
        val user = User("user1", 0)
        for (i in 1..10) {
            seats.add(makeDummySeat(i))
        }
        every { ticketRepository.save(any(Ticket::class)) } returns Ticket("ticket", User("user1", 0), makeDummySeat(1), Ticket.Status.WAITING_PAYMENT)

        //when
        val result = ticketManager.reserve(user, seats)

        //then
        assertThat(result.size).isEqualTo(10)
        assertThat(result.first()).isInstanceOf(Ticket::class.java)
    }

    @Test
    fun `결제 처리 테스트`(){
    	//given
        val user = User("user1", 10000)
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        val seats = mutableListOf<Seat>()
        for (i in 1..3) {
            seats.add(makeDummySeat(i))
        }
        val tickets = Ticket.makeTickets(user, seats.toList())
        every { ticketRepository.findAllByIds(ticketIds) } returns tickets
        assertThat(tickets.all { ticket -> ticket.status == Ticket.Status.WAITING_PAYMENT }).isTrue()

        //when
        val result = ticketManager.pay(user, ticketIds)
        assertThat(result.all { ticket -> ticket.status == Ticket.Status.COMPLETE_PAYMENT }).isTrue()
    }

    @Test
    fun `결제 처리 실패 - 예약한 유저와 결체 요청 유저가 다른 경우`(){
        //given
        val requestUser = User("user1", 10000)
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        val seats = mutableListOf<Seat>()
        for (i in 1..3) {
            seats.add(makeDummySeat(i))
        }
        val reserveUser = User("user2", 0)
        val tickets = Ticket.makeTickets(reserveUser, seats.toList())
        every { ticketRepository.findAllByIds(ticketIds) } returns tickets

        //when
        val throws = assertThrows<TicketException> { ticketManager.pay(requestUser, ticketIds) }
        assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.NOT_TICKET_OWNER)
    }

    @Test
    fun `결제 처리 실패 - 티켓 아이디가 올바르지 않은 경우`(){
        //given
        val user = User("user1", 10000)
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        every { ticketRepository.findAllByIds(ticketIds) } returns null

        //when
        val throws = assertThrows<TicketException> { ticketManager.pay(user, ticketIds) }
        assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.TICKET_NOT_FOUND)
    }

    @Test
    fun `결제 처리 실패 - 유저 포인트가 충분하지 않은 경우`(){
        //given
        val user = User("user1", 0)
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        val seats = mutableListOf<Seat>()
        for (i in 1..3) {
            seats.add(makeDummySeat(i))
        }
        val tickets = Ticket.makeTickets(user, seats.toList())
        every { ticketRepository.findAllByIds(ticketIds) } returns tickets

        //when
        val throws = assertThrows<TicketException> { ticketManager.pay(user, ticketIds) }
        assertThat(throws.errorResult).isEqualTo(TicketException.TicketErrorResult.USER_POINT_NOT_ENOUGH)
    }



    private fun makeDummySeat(i: Int) =
        Seat(
            "s1",
            Event("event1", "loc", LocalDate.now(), Concert("con1", "name", "singer"), mutableListOf<Seat>()),
            i.toString(),
            1000,
            Seat.Status.AVAILABLE
        )

}
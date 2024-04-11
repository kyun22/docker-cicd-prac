package kr.shlee.domain.ticket.model

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import kotlin.test.Test

class TicketTest {
    @Test
    fun `Seats를 받아 Ticket 생성`(){
    	// given
        val seats = mutableListOf<Seat>()
        val user = User("user1", 0)
        for (i in 1..10) {
            seats.add(
                Seat("s1",
                    Event("event1", "loc", LocalDate.now(), Concert("con1", "name", "singer"), mutableListOf<Seat>()),
                    i.toString(),
                    10000,
                    Seat.Status.AVAILABLE))
        }

        //when
        val tickets = Ticket.makeTickets(user, seats.toList())

        //then
        assertThat(tickets.size).isEqualTo(10)
        assertThat(tickets.first().user.id).isEqualTo(tickets.last().user.id).isEqualTo("user1")
    }


}
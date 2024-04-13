package kr.shlee.domain.event.model

import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import kotlin.test.Test

class EventTest {
    @Test
    fun `좌석 추가 테스트`(){
        //given
        val event = make10SeatsEvent("event1", LocalDate.now())

        //when
        for (i in 1..10) {
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, null, Seat.Status.PURCHASED))
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, null, Seat.Status.RESERVED))
        }

        //then
        assertThat(event.seats.size).isEqualTo(30)
    }

    @Test
    fun `예매 가능 좌석 수 확인 테스트`(){
        //given
        val event = make10SeatsEvent("event1", LocalDate.now())
        for (i in 1..10) {
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, null, Seat.Status.PURCHASED))
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, null, Seat.Status.RESERVED))
        }

        //when
        val seatCount = event.getAvailableSeatCount()

        //then
        assertThat(seatCount).isEqualTo(10)
    }

    private fun make10SeatsEvent(eventId: String, localDate: LocalDate): Event {
        val event = Event(eventId, "서울", localDate, Concert("concert1", "콘서트1", "아이유"))
        for (i in 1..10) {
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, null, Seat.Status.AVAILABLE))
        }
        return event
    }
}
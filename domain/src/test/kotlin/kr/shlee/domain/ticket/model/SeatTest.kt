package kr.shlee.domain.ticket.model

import kr.shlee.domain.event.model.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.Test

class SeatTest {
    @Test
    fun `reserve 상태 변경 테스트`(){
        //given
        val seat = Seat(
            "seat1",
            Event("event1", "loca", LocalDate.now(),
                Concert("concert1", "콘서트", "아이유")),
            "1",
            10000,
            null,
            Seat.Status.AVAILABLE
        )

        //when
        seat.changeToReserved()

        //then
        assertThat(seat.status).isEqualTo(Seat.Status.RESERVED)
        assertThat(seat.reservedAt).isNotNull()
    }

    @Test
    fun `refresh 테스트`(){
        val seat = Seat(
            "seat1",
            Event("event1", "loca", LocalDate.now(),
                Concert("concert1", "콘서트", "아이유")),
            "1",
            10000,
            LocalDateTime.now().minusMinutes(6),
            Seat.Status.RESERVED
        )

        //when
        seat.refreshStatus()

        //then
        assertThat(seat.status).isEqualTo(Seat.Status.AVAILABLE)
        assertThat(seat.reservedAt).isNull()

    }

}
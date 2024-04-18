package kr.shlee.api.event.usecase

import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.repository.SeatRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class EventSearchByIdUseCaseTest {

    @Autowired
    lateinit var eventSearchByIdUseCase: EventSearchByIdUseCase

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var seatRepository: SeatRepository

    @BeforeEach
    fun setUp() {
        val concert = Concert("concert1", "콘서트", "아이유")
        val event = Event("event1", "서울", LocalDate.now(), concert)
        eventRepository.save(event)
        seatRepository.save(Seat("seat1", event, "1", 1000, null, Seat.Status.AVAILABLE))
        seatRepository.save(Seat("seat2", event, "2", 1000, null, Seat.Status.AVAILABLE))
        seatRepository.save(Seat("seat3", event, "3", 1000, null, Seat.Status.AVAILABLE))
        seatRepository.save(Seat("seat4", event, "4", 1000, null, Seat.Status.AVAILABLE))
        seatRepository.save(Seat("seat5", event, "5", 1000, null, Seat.Status.PURCHASED))
    }

    @Test
    fun `이벤트 조회 테스트 실패 - 존재하지 않는 ID`() {
        //when
        val throws = assertThrows<EventException> { eventSearchByIdUseCase("event2") }
        assertThat(throws.errorResult).isEqualTo(EventException.EventErrorResult.RESULT_IS_EMPTY)
    }

    @Test
    fun `이벤트 조회 성공 - 존재하는 ID`(){
        val eventResponse = eventSearchByIdUseCase("event1")
        assertThat(eventResponse.id).isEqualTo("event1")
        assertThat(eventResponse.name).isEqualTo("콘서트")
        assertThat(eventResponse.location).isEqualTo("서울")
        assertThat(eventResponse.seats.size).isEqualTo(5)
        assertThat(eventResponse.availableSeats).isEqualTo(4)
    }


}
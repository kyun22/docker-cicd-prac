package kr.shlee.domain.ticket.component

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.common.util.DateUtils
import kr.shlee.domain.event.component.EventFinder
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.ticket.model.Concert
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class EventFinderTest {

    @MockK
    lateinit var eventRepository: EventRepository

    @InjectMockKs
    lateinit var eventFinder: EventFinder

    @Test
    fun `Event 검색 - 검색조건이 없는 경우 모든 이벤트를 반환한다`() {
        // given
        val events = listOf(
            makeDummyEvent("event1", LocalDate.now()),
            makeDummyEvent("event2", LocalDate.now()),
            makeDummyEvent("event3", LocalDate.now()))
        every { eventRepository.findAll() } returns events

        //when
        val result = eventFinder.findAll()

        //then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].seats.size).isEqualTo(10)
    }

    private fun makeDummyEvent(eventId: String, localDate: LocalDate): Event {
        val event = Event(eventId, "서울", localDate, Concert("concert1", "콘서트1", "아이유", null))
        for (i in 1..10) {
            event.addSeat(Seat(i.toString(), event, i.toString(), 10000, Seat.Status.AVAILABLE))
        }
        return event
    }

    @Test
    fun `Event 검색 - eventId로 찾기`() {
        //given
        every { eventRepository.findById("user1") } returns makeDummyEvent("event1", LocalDate.now())

        //when
        val event = eventFinder.find("user1")!!

        //then
        assertThat(event.id).isEqualTo("event1")
    }

    @Test
    fun `Event 검색 실패 - eventId로 찾기`(){
        //given
        every { eventRepository.findById("user1") } returns null

        //when
        assertThrows<EventException> { eventFinder.find("user1") }
    }

    @Test
    fun `Event 검색 - Date로 찾기`() {
        //given
        val dateString = "2023-03-25"
        val events = listOf(
            makeDummyEvent("event1", DateUtils.convertStringToLocalDate("2023-03-25")),
            makeDummyEvent("event2", DateUtils.convertStringToLocalDate("2023-03-25")))
        every { eventRepository.findByDate(DateUtils.convertStringToLocalDate(dateString)) } returns events

        //when
        val findByDate = eventFinder.findByDate(dateString)

        //then
        assertThat(findByDate.size).isEqualTo(2)
        assertThat(findByDate[0].id).isEqualTo("event1")
        assertThat(findByDate[1].id).isEqualTo("event2")

    }

    @Test
    fun `Event 검색 실패 - 해당 Date의 Event가 존재하지 않는 경우`(){
        //given
        every { eventRepository.findById("user1") } returns null

        //when
        assertThrows<EventException> { eventFinder.find("user1") }
    }

}
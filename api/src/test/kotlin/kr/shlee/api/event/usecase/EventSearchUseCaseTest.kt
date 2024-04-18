package kr.shlee.api.event.usecase

import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class EventSearchUseCaseTest {
    @Autowired
    lateinit var eventSearchUseCase: EventSearchUseCase

    @Autowired
    lateinit var eventRepository: EventRepository

    @BeforeEach
    fun setUp() {
        val concert = Concert("concert1", "콘서트", "아이유")
        val event = Event("event1", "서울", LocalDate.now(), concert)
        eventRepository.save(event)
    }

    @Test
    fun `이벤트 전체 조회 테스트`() {
        //when
        val eventList = eventSearchUseCase()
        eventList.forEach { event -> println(event) }
    }

}
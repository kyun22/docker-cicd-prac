package kr.shlee.api.event.usecase

import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.common.util.DateUtils
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.ticket.model.Concert
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class EventSearchByDateUseCaseTest {

    @Autowired lateinit var eventSearchByDateUseCase: EventSearchByDateUseCase

    @Autowired lateinit var eventRepository: EventRepository

    @Test
    fun `날짜로 이벤트 찾기 성공`(){
        //given
        val concert1 = Concert("concert1", "아이유의 하이유 콘서트", "아이유")
        val concert2 = Concert("concert2", "태양의 태양을 피하고싶어서 흠뻑쇼", "태양")
        val targetLocalDate = DateUtils.convertStringToLocalDate("2023-05-05")
        eventRepository.save(Event("event1", "seoul", targetLocalDate, concert1))
        eventRepository.save(Event("event2", "부산", DateUtils.convertStringToLocalDate("2023-05-06"), concert1))
        eventRepository.save(Event("event3", "LA", DateUtils.convertStringToLocalDate("2023-05-07"), concert1))
        eventRepository.save(Event("event4", "인도", targetLocalDate, concert2))
        eventRepository.save(Event("event5", "힝헤", DateUtils.convertStringToLocalDate("2023-05-06"), concert2))
        eventRepository.save(Event("event6", "잠실", DateUtils.convertStringToLocalDate("2023-05-07"), concert2))

    	//when
        val eventListResponse = eventSearchByDateUseCase("2023-05-05")

        //then
        assertThat(eventListResponse.size).isEqualTo(2)
        assertThat(eventListResponse[0].date).isEqualTo("2023-05-05")
    }

    @Test
    fun `날짜로 이벤트 찾기 실패 - 해당 날짜에 이벤트 없음`(){
        //given
        val concert1 = Concert("concert1", "아이유의 하이유 콘서트", "아이유")
        val concert2 = Concert("concert2", "태양의 태양을 피하고싶어서 흠뻑쇼", "태양")
        val targetLocalDate = DateUtils.convertStringToLocalDate("2023-05-05")
        eventRepository.save(Event("event1", "seoul", targetLocalDate, concert1))
        eventRepository.save(Event("event2", "부산", DateUtils.convertStringToLocalDate("2023-05-06"), concert1))
        eventRepository.save(Event("event3", "LA", DateUtils.convertStringToLocalDate("2023-05-07"), concert1))
        eventRepository.save(Event("event4", "인도", targetLocalDate, concert2))
        eventRepository.save(Event("event5", "힝헤", DateUtils.convertStringToLocalDate("2023-05-06"), concert2))
        eventRepository.save(Event("event6", "잠실", DateUtils.convertStringToLocalDate("2023-05-07"), concert2))

        //when
        val throws = assertThrows<EventException> { eventSearchByDateUseCase("2023-05-08") }
        assertThat(throws.errorResult).isEqualTo(EventException.EventErrorResult.RESULT_IS_EMPTY)
    }
}
package kr.shlee.api.event

import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.api.event.controller.EventController
import kr.shlee.api.event.dto.EventResponse
import kr.shlee.api.event.dto.SeatVo
import kr.shlee.api.event.usecase.EventSearchByDateUseCase
import kr.shlee.api.event.usecase.EventSearchByIdUseCase
import kr.shlee.api.event.usecase.EventSearchUseCase
import kr.shlee.domain.common.error.EventException
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.Test

class EventControllerTest {
    private lateinit var mockMvc: MockMvc
    private val eventSearchByIdUseCase: EventSearchByIdUseCase = mockk()
    private val eventSearchByDateUseCase: EventSearchByDateUseCase = mockk()
    private val eventSearchUseCase: EventSearchUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
            EventController(
                eventSearchByIdUseCase,
                eventSearchByDateUseCase,
                eventSearchUseCase
            )
        )
            .setControllerAdvice(ApiControllerAdvice()).build()
    }

    @Test
    fun `이벤트 조회 실패 - 토큰이 존재하지 않음`() {
        every {
            eventSearchUseCase.execute(null)
        } throws EventException(EventException.EventErrorResult.MISSING_TOKEN)
        mockMvc.perform(
            get("/events")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `이벤트 조회 실패 - 토큰이 유효하지 않음`() {
        every {
            eventSearchUseCase.execute("token0")
        } throws EventException(EventException.EventErrorResult.INVALID_TOKEN)
        mockMvc.perform(
            get("/events")
                .header("X-USER-TOKEN", "token0")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        ).andExpect(status().isUnauthorized)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun makeDymmySeatVos(): MutableList<SeatVo> {
        val seats = mutableListOf<SeatVo>()
        for (i in 1..10) {
            val seat = Seat(
                i.toString(),
                Event("event1", "loc", LocalDate.now(), Concert("concert1", "콘서트1", "아이유")),
                i.toString(),
                1000,
                Seat.Status.AVAILABLE
            )
            seats.add(SeatVo.of(seat))
        }
        for (i in 11..20) {
            val seat = Seat(
                i.toString(),
                Event("event1", "loc", LocalDate.now(), Concert("concert1", "콘서트1", "아이유")),
                i.toString(),
                1000,
                Seat.Status.RESERVED
            )
            seats.add(SeatVo.of(seat))
        }
        for (i in 21..30) {
            val seat = Seat(
                i.toString(),
                Event("event1", "loc", LocalDate.now(), Concert("concert1", "콘서트1", "아이유")),
                i.toString(),
                1000,
                Seat.Status.PURCHASED
            )
            seats.add(SeatVo.of(seat))
        }
        return seats
    }

    @Test
    fun `예약 가능 이벤트 조회 - 날짜로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"))
        every { eventSearchByDateUseCase.execute(dateString = "2024-03-25", token = "token1") } returns events
        mockMvc.perform(
            get("/events/dates/{dateString}", "2024-03-25")
                .header("X-USER-TOKEN", "token1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
    }

    @Test
    fun `예약 가능 이벤트 조회 - 아이디로`() {
        val event = EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25")
        every { eventSearchByIdUseCase.execute(eventId = "event1", token = "token1") } returns event
        mockMvc.perform(
            get("/events/{eventId}", "event1")
                .header("X-USER-TOKEN", "token1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
//            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("id").value("event1"))
            .andExpect(jsonPath("name").value("이벤트1"))
    }

    @Test
    fun `전체 예약 가능 이벤트 조회 - 검색조건 없음`() {
        val events = listOf<EventResponse>(
            EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"),
            EventResponse("event2", "이벤트2", "서울", 10, makeDymmySeatVos(), "2024-03-26"),
            EventResponse("event3", "이벤트3", "서울", 10, makeDymmySeatVos(), "2024-03-27"),
        )
        every { eventSearchUseCase.execute(token = "token1") } returns events
        mockMvc.perform(get("/events").header("X-USER-TOKEN", "token1"))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(3))
    }
}
package event

import advice.ApiControllerAdvice
import advice.WaitlistException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import waitlist.models.Event
import waitlist.models.Seat
import java.time.LocalDateTime
import kotlin.test.Test

class EventControllerTest {
    private lateinit var mockMvc: MockMvc
    private val eventSearchUseCase: EventSearchUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(EventController(eventSearchUseCase))
            .setControllerAdvice(ApiControllerAdvice()).build()
    }

    @Test
    fun `이벤트 조회 실패 - 토큰이 존재하지 않음`() {
        every { eventSearchUseCase.execute("2024-03-25", "event1", null)
        } throws WaitlistException(WaitlistException.WaitlistErrorResult.MISSING_TOKEN)
        mockMvc.perform(
            get("/events")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `이벤트 조회 실패 - 토큰이 유효하지 않음`() {
        every { eventSearchUseCase.execute("2024-03-25", "event1", "token0")
        } throws WaitlistException(WaitlistException.WaitlistErrorResult.INVALID_TOKEN)
        val resultActions = mockMvc.perform(
            get("/events")
                .header("X-USER-TOKEN", "token0")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        ).andExpect(status().isUnauthorized)
    }



    @Test
    fun `예약 가능한 티켓을 조회한다`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"))
        every { eventSearchUseCase.execute("2024-03-25", "event1", "token1") } returns events
        mockMvc.perform(
            get("/events")
                .header("X-USER-TOKEN", "token1")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
            .andExpect(jsonPath("$[0].availableSeats").value(10))
    }

    private fun makeDymmySeatVos(): MutableList<SeatVo> {
        val seats = mutableListOf<SeatVo>()
        for (i in 1..10) {
            val seat = Seat(
                i.toLong(),
                Event("event1", "name", "loc", LocalDateTime.now()),
                i.toString(),
                1000,
                Seat.SeatStatus.AVAILABLE
            )
            seats.add(SeatVo.of(seat))
        }
        for (i in 11..20) {
            val seat = Seat(
                i.toLong(),
                Event("event1", "name", "loc", LocalDateTime.now()),
                i.toString(),
                1000,
                Seat.SeatStatus.RESERVED
            )
            seats.add(SeatVo.of(seat))
        }
        for (i in 21..30) {
            val seat = Seat(
                i.toLong(),
                Event("event1", "name", "loc", LocalDateTime.now()),
                i.toString(),
                1000,
                Seat.SeatStatus.PURCHASED
            )
            seats.add(SeatVo.of(seat))
        }
        return seats
    }

    @Test
    fun `예약 가능 이벤트 조회 - 날짜로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"))
        every { eventSearchUseCase.execute(date = "2024-03-25", null, "token1") } returns events
        mockMvc.perform(
            get("/events")
                .header("X-USER-TOKEN", "token1")
                .param("date", "2024-03-25")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
    }

    @Test
    fun `예약 가능 이벤트 조회 - 아이디로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"))
        every { eventSearchUseCase.execute(date = null, eventId = "event1", token = "token1") } returns events
        mockMvc.perform(
            get("/events")
                .header("X-USER-TOKEN", "token1")
                .param("eventId", "event1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
    }

    @Test
    fun `전체 예약 가능 이벤트 조회 - 검색조건 없음`() {
        val events = listOf<EventResponse>(
            EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"),
            EventResponse("event2", "이벤트2", "서울", 10, makeDymmySeatVos(), "2024-03-26"),
            EventResponse("event3", "이벤트3", "서울", 10, makeDymmySeatVos(), "2024-03-27"),
        )
        every { eventSearchUseCase.execute(date = null, eventId = null, token = "token1") } returns events
        mockMvc.perform(get("/events").header("X-USER-TOKEN", "token1"))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(3))
    }
}
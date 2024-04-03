package ticket

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

class EventControllerTest {
    private lateinit var mockMvc: MockMvc
    private val eventSearchUseCase: EventSearchUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(EventController(eventSearchUseCase)).build()
    }

    @Test
    fun `예약 가능한 티켓을 조회한다`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", "2024-03-25"))
        every { eventSearchUseCase.execute("2024-03-25", "event1") } returns events
        mockMvc.perform(
            get("/events")
                .param("date", "2024-03-25")
                .param("eventId", "event1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
    }

    @Test
    fun `예약 가능 이벤트 조회 - 날짜로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", "2024-03-25"))
        every { eventSearchUseCase.execute(date = "2024-03-25", null) } returns events
        mockMvc.perform(
            get("/events")
                .param("date", "2024-03-25")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
    }

    @Test
    fun `예약 가능 이벤트 조회 - 아이디로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", "2024-03-25"))
        every { eventSearchUseCase.execute(date = null, eventId = "event1") } returns events
        mockMvc.perform(
            get("/events")
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
            EventResponse("event1", "이벤트1", "서울", "2024-03-25"),
            EventResponse("event2", "이벤트2", "서울", "2024-03-26"),
            EventResponse("event3", "이벤트3", "서울", "2024-03-27"),
        )
        every { eventSearchUseCase.execute(date = null, eventId = null) } returns events
        mockMvc.perform(get("/events"))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(3))
    }
}
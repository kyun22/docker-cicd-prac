package kr.shlee.api.event.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.api.event.dto.EventResponse
import kr.shlee.api.event.usecase.EventSearchByDateUseCase
import kr.shlee.api.event.usecase.EventSearchByIdUseCase
import kr.shlee.api.event.usecase.EventSearchUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import kotlin.test.Test

@ExtendWith(RestDocumentationExtension::class)
class EventControllerTest {
    private lateinit var mockMvc: MockMvc
    private val eventSearchByIdUseCase: EventSearchByIdUseCase = mockk()
    private val eventSearchByDateUseCase: EventSearchByDateUseCase = mockk()
    private val eventSearchUseCase: EventSearchUseCase = mockk()

    @BeforeEach
    fun setUp(restDocumentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.standaloneSetup(
            EventController(
                eventSearchByIdUseCase,
                eventSearchByDateUseCase,
                eventSearchUseCase
            )
        )
            .setControllerAdvice(ApiControllerAdvice())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build()
    }

    @Test
    fun `예약 가능 이벤트 조회 - 날짜로`() {
        val events = listOf<EventResponse>(EventResponse("event1", "이벤트1", "서울", 10, emptyList(),"2024-03-25"))
        every { eventSearchByDateUseCase(dateString = "2024-03-25") } returns events
        mockMvc.perform(
            get("/events/dates/{dateString}", "2024-03-25")
                .header("X-USER-TOKEN", "token1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("event1"))
            .andDo(document("event-search-by-date"))
    }

    @Test
    fun `예약 가능 이벤트 조회 - 아이디로`() {
        val event = EventResponse("event1", "이벤트1", "서울", 10, emptyList(), "2024-03-25")
        every { eventSearchByIdUseCase(eventId = "event1") } returns event
        mockMvc.perform(
            get("/events/{eventId}", "event1")
                .header("X-USER-TOKEN", "token1")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
//            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("id").value("event1"))
            .andExpect(jsonPath("name").value("이벤트1"))
            .andDo(document("event-search-by-id"))
    }

    @Test
    fun `전체 예약 가능 이벤트 조회 - 검색조건 없음`() {
        val events = listOf<EventResponse>(
            EventResponse("event1", "이벤트1", "서울", 10, emptyList(), "2024-03-25"),
            EventResponse("event2", "이벤트2", "서울", 10, emptyList(), "2024-03-26"),
            EventResponse("event3", "이벤트3", "서울", 10, emptyList(), "2024-03-27"),
        )
        every { eventSearchUseCase() } returns events
        mockMvc.perform(
            get("/events").header("X-USER-TOKEN", "token1"))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.length()").value(3))
            .andDo(document("event-search-all"))

    }
}
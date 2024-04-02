package controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import waitlist.controller.WaitlistController
import waitlist.dto.TokenRequestDto
import waitlist.dto.WaitlistResponse
import waitlist.models.WaitlistStatus
import waitlist.usecase.WaitlistCheckOrderUseCase
import waitlist.usecase.WaitlistRegisterUseCase
import java.util.UUID

class WaitlistControllerTest {

    private lateinit var mockMvc: MockMvc
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val waitlistRegisterUseCase: WaitlistRegisterUseCase = mockk()
    private val waitlistCheckOrderUseCase: WaitlistCheckOrderUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
            WaitlistController(
                waitlistRegisterUseCase,
                waitlistCheckOrderUseCase
                )).build()
    }

    @Test
    fun `mock - 토큰을 발급한다`() {
        //given
        val request: TokenRequestDto = TokenRequestDto("user1", "event1")
        every { waitlistRegisterUseCase.execute(request) } returns WaitlistResponse(
            token = UUID.randomUUID().toString(),
            userId = request.userId,
            eventId = request.eventId,
            position = 0,
            status = WaitlistStatus.WAITING
        )
        val json = objectMapper.writeValueAsString(request)

        //when
        mockMvc.perform(
            post("/waitlist")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("userId").value("user1"))
            .andDo(print())
    }


    @Test
    fun `mock - 대기순번을 조회한다`(){
        //given
        every { waitlistCheckOrderUseCase.execute("user1", "event1") } returns WaitlistResponse(
            token = UUID.randomUUID().toString(),
            userId = "user1",
            eventId = "event1",
            position = 0,
            status = WaitlistStatus.WAITING
        )

        //when
        mockMvc.perform(get("/waitlist/{userId}/{eventId}", "user1", "event1"))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("userId").value("user1"))
            .andDo(print())
    }

}


package kr.shlee.waitlist.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import kr.shlee.advice.ApiControllerAdvice
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.dto.WaitlistResponse
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.usecase.WaitlistCheckOrderUseCase
import kr.shlee.waitlist.usecase.WaitlistRegisterUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

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
            )
        )
            .setControllerAdvice(ApiControllerAdvice())
            .build()
    }

    @Test
    fun `mock - 토큰을 발급한다`() {
        //given
        val request = WaitlistRequest.Register("user1")
        every { waitlistRegisterUseCase.execute(request) } returns WaitlistResponse.Register(
            token = UUID.randomUUID().toString(),
            userId = request.userId,
            status = Waitlist.Status.WAITING,
            no = 0
        )
        val json = objectMapper.writeValueAsString(request)

        //when
        mockMvc.perform(
            post("/waitlist/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("userId").value("user1"))
            .andDo(print())
    }


    @Test
    fun `mock - 대기순번을 조회한다`() {
        //given
        val request = WaitlistRequest.Position("user1")
        val json = objectMapper.writeValueAsString(request)
        every { waitlistCheckOrderUseCase.execute(request) } returns WaitlistResponse.Position(
            position = 2,
        )

        //when
        mockMvc.perform(get("/waitlist/position")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("position").value(2))
            .andDo(print())
    }

}


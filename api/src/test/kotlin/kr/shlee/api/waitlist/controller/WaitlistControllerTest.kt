package kr.shlee.api.waitlist.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.api.waitlist.dto.WaitlistResponse
import kr.shlee.api.waitlist.usecase.WaitlistCheckOrderUseCase
import kr.shlee.api.waitlist.usecase.WaitlistRegisterUseCase
import kr.shlee.domain.waitlist.model.Waitlist
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.util.*

@ExtendWith(RestDocumentationExtension::class)
class WaitlistControllerTest {

    private lateinit var mockMvc: MockMvc
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val waitlistRegisterUseCase: WaitlistRegisterUseCase = mockk()
    private val waitlistCheckOrderUseCase: WaitlistCheckOrderUseCase = mockk()

    @BeforeEach
    fun setUp(restDocumentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.standaloneSetup(
            WaitlistController(
                waitlistRegisterUseCase,
                waitlistCheckOrderUseCase
            )
        )
            .setControllerAdvice(ApiControllerAdvice())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
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
            RestDocumentationRequestBuilders.post("/waitlist/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("userId").value("user1"))
            .andDo(MockMvcRestDocumentationWrapper.document("waitlist-token-generate"))
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
        mockMvc.perform(RestDocumentationRequestBuilders.get("/waitlist/position")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("position").value(2))
            .andDo(MockMvcRestDocumentationWrapper.document("check-waitlist-position"))
    }

}


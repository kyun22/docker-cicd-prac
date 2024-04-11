package kr.shlee.api.point

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.domain.point.model.User
import kr.shlee.api.point.controller.PointController
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.api.point.usecase.PointChargeUseCase
import kr.shlee.api.point.usecase.PointCheckUseCase
import kr.shlee.domain.common.error.PointException
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

class PointControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()
    private val pointChargeUseCase: PointChargeUseCase = mockk()
    private val pointCheckUseCase: PointCheckUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(PointController(pointChargeUseCase, pointCheckUseCase))
            .setControllerAdvice(ApiControllerAdvice()).build()
    }

    @Test
    fun `포인트 충전 테스트`() {
        //given
        val request = PointRequest.Charge("user1", 10000)
        val json = objectMapper.writeValueAsString(request)
        every { pointChargeUseCase.execute(any(PointRequest.Charge::class)) } returns User(
            "user1",
            10000
        )
        //when
        mockMvc.perform(
            post("/points/{userId}/charge", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value("user1"))
            .andExpect(jsonPath("point").value(10000))
    }


    @Test
    fun `포인트 조회 실패 - 존재하지 않는 유저`(){
        every { pointCheckUseCase.execute("user0") } throws PointException(PointException.PointErrorResult.USER_NOT_EXISTS)
        mockMvc.perform(get("/points/{userId}/check", "user0"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `포인트 조회 테스트`(){
        every { pointCheckUseCase.execute("user1") } returns User(
            "user1",
            10000
        )
        mockMvc.perform(get("/points/{userId}/check", "user1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value("user1"))
            .andExpect(jsonPath("point").value(10000))
            .andDo(MockMvcResultHandlers.print())
    }


}
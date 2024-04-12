package kr.shlee.api.point

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.api.point.controller.PointController
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.api.point.usecase.PointChargeUseCase
import kr.shlee.api.point.usecase.PointCheckUseCase
import kr.shlee.domain.common.error.PointException
import kr.shlee.domain.point.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import kotlin.test.Test

@ExtendWith(RestDocumentationExtension::class)
class PointControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()
    private val pointChargeUseCase: PointChargeUseCase = mockk()
    private val pointCheckUseCase: PointCheckUseCase = mockk()

    @BeforeEach
    fun setUp(restDocumentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(
            PointController(pointChargeUseCase, pointCheckUseCase))
            .setControllerAdvice(ApiControllerAdvice())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build()
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
            RestDocumentationRequestBuilders.post("/points/{userId}/charge", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value("user1"))
            .andExpect(jsonPath("point").value(10000))
            .andDo(
                document(
                "identifier",
                resourceDetails = ResourceSnippetParametersBuilder()
                    .tag("tag")
                    .pathParameters(
                        parameterWithName("userId").description("유저 ID")
                    )
                    .responseFields(
                        fieldWithPath("id").type(JsonFieldType.STRING),
                        fieldWithPath("point").type(JsonFieldType.NUMBER)
                    )
            )
            )
    }


    @Test
    fun `포인트 조회 실패 - 존재하지 않는 유저`(){
        every { pointCheckUseCase.execute("user0") } throws PointException(PointException.PointErrorResult.USER_NOT_EXISTS)
        mockMvc.perform(get("/points/{userId}/check", "user0"))
            .andExpect(status().isBadRequest)
            .andDo(document("check-point-fail-user-not-found"))
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
            .andDo(document("check-point-success"))
    }


}
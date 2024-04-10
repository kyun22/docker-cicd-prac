package kr.shlee.api.waitlist

import kr.shlee.api.config.WaitlistTokenInterceptor
import kr.shlee.domain.waitlist.component.WaitlistTokenValidator
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class WaitlistTokenInterceptorTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenValidator: WaitlistTokenValidator

    @MockBean lateinit var interceptor: WaitlistTokenInterceptor

    @Test
    fun `토큰검증 인터셉터 성공 - 토큰이 유효한 경우 200을 반환`() {
        Mockito.`when`(tokenValidator.validate("validToken")).thenReturn(true)
        mockMvc.perform(get("/events").header("X-USER-TOKEN", "validToken"))
            .andExpect(status().isOk)
    }

    @Test
    fun `토큰검증 인터셉터 실패 - 토큰이 유효하지 않은 경우 401 반환`() {
        Mockito.`when`(tokenValidator.validate("invalidToken")).thenReturn(false)
        mockMvc.perform(get("/events").header("X-USER-TOKEN", "invalidToken"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @Disabled  // todo, point쪽 수정하고 다시 테스트
    fun `예외 path로 요청하는 경우 토큰검증 인터셉터가 동작하지 않음`(){
        //when
        mockMvc.perform(get("/points/{userId}", "user1").header("X-USER-TOKEN", "invalidToken"))

        //verify
        Mockito.verify(tokenValidator, never()).validate("invalidToken")
        Mockito.verify(interceptor, never()).preHandle(Mockito.any(), Mockito.any(), Mockito.any())
    }

}
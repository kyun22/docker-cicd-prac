package kr.shlee.api.waitlist

import kr.shlee.api.event.dto.EventResponse
import kr.shlee.api.event.dto.SeatVo
import kr.shlee.api.event.usecase.EventSearchUseCase
import kr.shlee.api.point.usecase.PointCheckUseCase
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.waitlist.component.WaitlistTokenValidator
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
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class WaitlistTokenInterceptorTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenValidator: WaitlistTokenValidator

    @MockBean lateinit var eventSearchUseCase: EventSearchUseCase

    @MockBean lateinit var pointCheckUseCase: PointCheckUseCase

    @Test
    fun `토큰검증 인터셉터 성공 - 토큰이 유효한 경우 200을 반환`() {
        val events = listOf<EventResponse>(
            EventResponse("event1", "이벤트1", "서울", 10, makeDymmySeatVos(), "2024-03-25"),
        )
        Mockito.`when`(tokenValidator.validate("validToken")).thenReturn(true)
        Mockito.`when`(eventSearchUseCase.execute("validToken")).thenReturn(events)
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
    fun `예외 path로 요청하는 경우 토큰검증 인터셉터가 동작하지 않음`(){
        //given
        Mockito.`when`(pointCheckUseCase.execute("user1")).thenReturn(User("user1", 0))

        //when
        mockMvc.perform(get("/points/{userId}", "user1").header("X-USER-TOKEN", "invalidToken"))

        //verify
        Mockito.verify(tokenValidator, never()).validate("invalidToken")
    }

    private fun makeDymmySeatVos(): MutableList<SeatVo> {
        val seats = mutableListOf<SeatVo>()
        for (i in 1..10) {
            val seat = Seat(
                i.toString(),
                Event("event1", "loc", LocalDate.now(), Concert("concert1", "콘서트1", "아이유")),
                i.toString(),
                1000,
                null,
                Seat.Status.AVAILABLE
            )
            seats.add(SeatVo.of(seat))
        }
        return seats
    }

}
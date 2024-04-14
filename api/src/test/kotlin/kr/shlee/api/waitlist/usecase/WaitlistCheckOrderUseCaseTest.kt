package kr.shlee.api.waitlist.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.domain.waitlist.component.WaitlistReader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class WaitlistCheckOrderUseCaseTest {

    @MockK
    lateinit var waitlistReader: WaitlistReader

    @InjectMockKs
    lateinit var waitlistCheckOrderUseCase: WaitlistCheckOrderUseCase

    @Test
    fun `대기순번체크 테스트`(){
    	//given
        val req = WaitlistRequest.Position("token20")
        every { waitlistReader.getPosition(req.token) } returns 10L

        //when
        val res = waitlistCheckOrderUseCase.execute(req)

        //then
        assertThat(res.position).isEqualTo(10)
    }

}
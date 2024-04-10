package kr.shlee.waitlist.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.waitlist.components.WaitlistReader
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.models.Waitlist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
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
        every { waitlistReader.findByToken("token20") } returns Waitlist(20, "token20", "user2", LocalDateTime.now(), null, Waitlist.Status.WAITING)
        every { waitlistReader.getLastAvailableWaitlist() } returns Waitlist(10, "token10", "user1", LocalDateTime.now(), null, Waitlist.Status.AVAILABLE)

        //when
        val res = waitlistCheckOrderUseCase.execute(req)

        //then
        assertThat(res.position).isEqualTo(10)
    }

}
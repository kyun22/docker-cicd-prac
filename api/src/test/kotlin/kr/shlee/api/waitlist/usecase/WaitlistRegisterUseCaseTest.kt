package kr.shlee.api.waitlist.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import kr.shlee.domain.waitlist.component.WaitlistWriter
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.api.waitlist.dto.WaitlistRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WaitlistRegisterUseCaseTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var waitlistWriter: WaitlistWriter

    @MockK
    private lateinit var waitlistReader: WaitlistReader

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다 - 대기열에 빈자리가 없는 경우 WAITING`() {
        //given
        every { waitlistReader.findAlreadyRegistered("user1") } returns null
        every { waitlistReader.getAvailableWaitlistCount() } returns 60L
        every { waitlistWriter.add("user1", 60, 50)} returns Waitlist.newOf("user1")
        val request: WaitlistRequest.Register = WaitlistRequest.Register("user1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.status).isEqualTo(Waitlist.Status.WAITING)

    }

    @Test
    fun `userId로 토큰을 발급한다 - 대기열에 빈자리가 있어 바로 입장 가능`() {
        //given
        every { waitlistReader.findAlreadyRegistered("user1") } returns null
        every { waitlistReader.getAvailableWaitlistCount() } returns 40L
        every { waitlistWriter.add("user1", 40, 50)} returns Waitlist.newOf("user1").apply { changeStatus(Waitlist.Status.AVAILABLE) }
        val request: WaitlistRequest.Register = WaitlistRequest.Register("user1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.status).isEqualTo(Waitlist.Status.AVAILABLE)
    }

}



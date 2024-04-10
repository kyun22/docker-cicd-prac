package kr.shlee.waitlist.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.point.model.User
import kr.shlee.point.repository.UserRepository
import kr.shlee.waitlist.component.WaitlistAppender
import kr.shlee.waitlist.component.WaitlistReader
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.model.Waitlist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WaitlistRegisterUseCaseTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var waitlistAppender: WaitlistAppender

    @MockK
    private lateinit var waitlistReader: WaitlistReader

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다`() {
        //given
        every { userRepository.findById("user1") } returns User("user1", 0)
        every { waitlistReader.find("user1") } returns null
        every { waitlistAppender.add(any())} returns Waitlist.newOf("user1")
        every { waitlistReader.getAvailableCount() } returns 50L
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
        every { userRepository.findById("user1") } returns User("user1", 0)
        every { waitlistReader.find("user1") } returns null
        every { waitlistAppender.add(any())} returns Waitlist.newOf("user1")
        every { waitlistReader.getAvailableCount() } returns 40L
        val request: WaitlistRequest.Register = WaitlistRequest.Register("user1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.status).isEqualTo(Waitlist.Status.AVAILABLE)
    }

}



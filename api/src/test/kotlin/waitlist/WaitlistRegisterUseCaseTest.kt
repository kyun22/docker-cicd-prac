package waitlist

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.point.models.User
import kr.shlee.point.repositories.UserRepository
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.waitlist.repositories.WaitListRepository
import kr.shlee.waitlist.usecase.WaitlistRegisterUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WaitlistRegisterUseCaseTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다`() {
        //given
        every { userRepository.findById("user1") } returns User("user1", 0)
        every { waitListRepository.findByUserId("user1") } returns null
        every { waitListRepository.save(any())} returns Waitlist.newOf("user1")
        val request: WaitlistRequest = WaitlistRequest("user1", "event1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.status).isEqualTo(Waitlist.Status.WAITING)

    }

}



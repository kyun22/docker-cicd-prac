package waitlist

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import waitlist.dto.WaitlistRequest
import waitlist.models.WaitlistStatus
import waitlist.repositories.WaitListRepository
import waitlist.usecase.WaitlistRegisterUseCase

class UseCaseTest {
    @MockK
    private lateinit var eventRepository: WaitListRepository

    @MockK
    private lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다`() {
        val request: WaitlistRequest = WaitlistRequest("user1", "event1")
        val response = waitlistRegisterUseCase.execute(request)

        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.eventId).isEqualTo("event1")
        assertThat(response.status).isEqualTo(WaitlistStatus.WAITING)

    }

}



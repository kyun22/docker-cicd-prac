package waitlist

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import waitlist.dto.WaitlistRequest
import waitlist.models.Event
import waitlist.models.Waitlist
import waitlist.models.WaitlistStatus
import waitlist.repositories.EventRepository
import waitlist.repositories.WaitListRepository
import waitlist.usecase.WaitlistRegisterUseCase
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class WaitlistRegisterUseCaseTest {
    @MockK
    private lateinit var eventRepository: EventRepository

    @MockK
    private lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다`() {
        //given
        every { eventRepository.findById("event1") } returns Event("event1", "name", "location", LocalDateTime.now())
        every { waitListRepository.findByUserIdAndEventId("user1", "event1") } returns null
        every { waitListRepository.save(any())} returns Waitlist.newOf("user1", "event1")
        val request: WaitlistRequest = WaitlistRequest("user1", "event1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.eventId).isEqualTo("event1")
        assertThat(response.status).isEqualTo(WaitlistStatus.WAITING)

    }

}



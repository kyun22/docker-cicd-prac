package waitlist

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.ticket.models.Concert
import kr.shlee.ticket.models.Event
import kr.shlee.waitlist.models.Waitlist
import kr.shlee.ticket.repositories.EventRepository
import kr.shlee.waitlist.repositories.WaitListRepository
import kr.shlee.waitlist.usecase.WaitlistRegisterUseCase
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
        every { eventRepository.findById("event1") } returns Event("event1", "서울", LocalDateTime.now(), Concert("concert1", "콘서트1", "아이유"), mutableListOf())
        every { waitListRepository.findByUserIdAndEventId("user1", "event1") } returns null
        every { waitListRepository.save(any())} returns Waitlist.newOf("user1", "event1")
        val request: WaitlistRequest = WaitlistRequest("user1", "event1")

        //when
        val response = waitlistRegisterUseCase.execute(request)

        //then
        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.eventId).isEqualTo("event1")
        assertThat(response.status).isEqualTo(Waitlist.Status.WAITING)

    }

}



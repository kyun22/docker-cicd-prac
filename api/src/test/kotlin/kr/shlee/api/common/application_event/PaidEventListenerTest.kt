package kr.shlee.api.common.application_event

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.ticket.model.PaidEvent
import kr.shlee.domain.waitlist.component.WaitlistReader
import kr.shlee.domain.waitlist.component.WaitlistWriter
import kr.shlee.domain.waitlist.model.Waitlist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class PaidEventListenerTest {

    @MockK
    lateinit var waitlistReader: WaitlistReader

    @MockK
    lateinit var waitlistWriter: WaitlistWriter

    @InjectMockKs
    lateinit var paidEventListener: PaidEventListener

    @Test
    fun `PaidEvent 테스트 - 결제 완료 후 waitlist를 만료시킨다`(){
    	//given
        @SpyK
        val waitlist = Waitlist(1, "token1", "user1", LocalDateTime.now(), Waitlist.Status.AVAILABLE)
        every { waitlistReader.getByUserId("user1") } returns waitlist
        every { waitlistWriter.save(waitlist) } returns waitlist
        every { waitlistReader.findFirstWaitingWaitlist() } returns null
        assertThat(waitlist.status).isEqualTo(Waitlist.Status.AVAILABLE)

        //when
        paidEventListener.expireWaitlist(PaidEvent("user1"))

        //then
        assertThat(waitlist.status).isEqualTo(Waitlist.Status.EXPIRED)
    }

    @Test
    fun `PaidEvent 테스트 - 결제 완료 후 다음 waitlist를 입장시킨다`(){
        @SpyK
        val paid = Waitlist(1, "token1", "user1", LocalDateTime.now(), Waitlist.Status.AVAILABLE)
        @SpyK
        val waiting = Waitlist(2, "token1", "user1", LocalDateTime.now(), Waitlist.Status.WAITING)
        every { waitlistReader.getByUserId("user1") } returns paid
        every { waitlistWriter.save(paid) } returns paid
        every { waitlistReader.findFirstWaitingWaitlist() } returns waiting
        every { waitlistWriter.save(waiting) } returns waiting
        assertThat(paid.status).isEqualTo(Waitlist.Status.AVAILABLE)
        assertThat(waiting.status).isEqualTo(Waitlist.Status.WAITING)

        //when
        paidEventListener.expireWaitlist(PaidEvent("user1"))

        //then
        assertThat(paid.status).isEqualTo(Waitlist.Status.EXPIRED)
        assertThat(waiting.status).isEqualTo(Waitlist.Status.AVAILABLE)
    }

}
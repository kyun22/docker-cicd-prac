package kr.shlee.waitlist.model

import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import kotlin.test.Test

class WaitlistTest {
    @Test
    fun `status 변경 테스트`() {
        val waitlist = Waitlist.newOf("user1")
        assertThat(waitlist.status).isEqualTo(Waitlist.Status.WAITING)

        val after = waitlist.apply { changeStatus(Waitlist.Status.AVAILABLE) }
        assertThat(after.status).isEqualTo(Waitlist.Status.AVAILABLE)
        assertThat(after.updateStatusAt).isAfter(waitlist.createdAt)
    }

    @Test
    fun `대기순번 계산 테스트`(){
        val last = Waitlist(0, "token1", "user1", LocalDateTime.now(), null, Waitlist.Status.WAITING)
        val new = Waitlist(1, "token1", "user1", LocalDateTime.now(), null, Waitlist.Status.WAITING)

        assertThat(new.getPositionFromLastWaitlist(last)).isEqualTo(1L)
    }

}
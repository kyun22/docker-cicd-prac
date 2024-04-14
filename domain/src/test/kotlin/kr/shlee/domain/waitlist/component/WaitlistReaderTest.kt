package kr.shlee.domain.waitlist.component

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.common.error.WaitlistException
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class WaitlistReaderTest {
    @MockK
    lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    lateinit var waitlistReader: WaitlistReader

    @Test
    fun `미리 발급받은 토큰 가져오기 실패 - 발급받은 적 없는 유저`() {
        every { waitListRepository.findByUserId("user1") } returns null

        //when
        val waitlist = waitlistReader.findAlreadyRegistered("user1")

        //then
        assertThat(waitlist).isNull()
    }

    @Test
    fun `미리 발급받은 토큰 가져오기 실패 - 미리 발급 받았으나 만료된 경우`() {
        //given
        val find = Waitlist(1, "token1", "user1", LocalDateTime.now(),
            Waitlist.Status.EXPIRED
        )
        every { waitListRepository.findByUserId("user1") } returns find
        every { waitListRepository.findByIdExceptExpired("user1") } returns find

        //when
        val waitlist = waitlistReader.findAlreadyRegistered("user1")

        //then
        assertNotNull(waitlist)
        assertThat(waitlist?.status).isEqualTo(Waitlist.Status.EXPIRED)
    }

    @Test
    fun `미리 발급받은 토큰 가져오기 성공`() {
        //given
        val find = Waitlist(1, "token1", "user1", LocalDateTime.now(),
            Waitlist.Status.WAITING
        )
        every { waitListRepository.findByUserId("user1") } returns find
        every { waitListRepository.findByIdExceptExpired("user1") } returns find

        //when
        val waitlist = waitlistReader.findAlreadyRegistered("user1")

        //then
        assertNotNull(waitlist)
        assertThat(waitlist?.status).isEqualTo(Waitlist.Status.WAITING)
    }

    @Test
    fun `getByUserId 실패 - 존재하지 않는 경우 Exception을 던짐 `() {
        every { waitListRepository.findByUserId("user1") } returns null

        //when
        val exception = assertThrows<WaitlistException> { waitlistReader.getByUserId("user1") }
        assertThat(exception.errorResult).isEqualTo(WaitlistException.WaitlistErrorResult.USER_NOT_FOUND)
    }

    @Test
    fun `대기열 순서 체크 실패 - 토큰이 존재하지 않는 경우 예외 발생`() {
        every { waitListRepository.findByToken("token1") } returns null

        //when
        val exception = assertThrows<WaitlistException> { waitlistReader.getPosition("token1") }
        assertThat(exception.errorResult).isEqualTo(WaitlistException.WaitlistErrorResult.USER_NOT_FOUND)
    }

    @Test
    fun `대기열 순서 체크 0 - 대기열 avaiable 유저가 없는 경우`() {
        every { waitListRepository.findByToken("token1") } returns Waitlist(
            1,
            "token1",
            "user1",
            null,
            Waitlist.Status.WAITING
        )
        every { waitListRepository.getLastAvailableWaitlist() } returns null

        //when
        val position = waitlistReader.getPosition("token1")
        assertThat(position).isEqualTo(0)
    }

}
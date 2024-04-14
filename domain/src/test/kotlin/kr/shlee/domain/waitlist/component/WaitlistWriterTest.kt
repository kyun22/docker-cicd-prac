package kr.shlee.domain.waitlist.component

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class WaitlistWriterTest {
    @MockK
    lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    lateinit var waitlistWriter: WaitlistWriter

    @Test
    fun `대기열 추가 테스트 - 바로 입장`(){
        //given
        val activeCount = 10L
        val maxAvailableCount = 50L
        val expectedWaitlist = Waitlist.newOf("user1").apply {
            if (activeCount < maxAvailableCount) changeStatus(Waitlist.Status.AVAILABLE)
        }
        every { waitListRepository.save(any()) } returns expectedWaitlist

    	//when
        val new = waitlistWriter.add("user1", activeCount, maxAvailableCount)

        //then
        assertThat(new.status).isEqualTo(Waitlist.Status.AVAILABLE)
    }

    @Test
    fun `대기열 추가 테스트 - Waiting`(){
        //given
        val activeCount = 60L
        val maxAvailableCount = 50L
        every { waitListRepository.save(any()) } returns Waitlist.newOf("user1")

        //when
        val new = waitlistWriter.add("user1", activeCount, maxAvailableCount)

        //then
        assertThat(new.status).isEqualTo(Waitlist.Status.WAITING)
    }



}
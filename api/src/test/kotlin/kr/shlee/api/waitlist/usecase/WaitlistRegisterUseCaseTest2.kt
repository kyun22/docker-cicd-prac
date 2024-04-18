package kr.shlee.api.waitlist.usecase

import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import kr.shlee.domain.waitlist.model.Waitlist
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class WaitlistRegisterUseCaseTest2 {
    @Autowired lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase
    @Autowired lateinit var userManager: UserManager
    @Autowired lateinit var waitListRepository: WaitListRepository

    @Test
    fun `대기열 등록 성공 - 대기열에 자리가 있는 경우 AVAILABLE `(){
    	//given
        val user = userManager.save(User("user1", 0))
        val request = WaitlistRequest.Register(user.id)

        //when
        val response = waitlistRegisterUseCase(request)

        //then
        assertThat(response.userId).isEqualTo(user.id)
        assertThat(response.token).isNotEmpty()
        assertThat(response.status).isEqualTo(Waitlist.Status.AVAILABLE)
    }

    @Test
    fun `대기열 등록 실패 - 존재하지 않는 유저`(){
        val request = WaitlistRequest.Register("user1")

        //when
        val throws = assertThrows<UserException> { waitlistRegisterUseCase(request) }

        //then
        assertThat(throws.errorResult).isEqualTo(UserException.UserErrorResult.USER_NOT_FOUND)
    }

    @Test
    fun `이미 등록한 대기열이 존재하는 경우 그 토큰 응답`(){
        //given
        val user = userManager.save(User("user1", 0))
        val registerReq = WaitlistRequest.Register(user.id)
        val registerBefore = waitlistRegisterUseCase(registerReq)
        val oneMoreRegisterReq = WaitlistRequest.Register(user.id)

        //when
        val response = waitlistRegisterUseCase(oneMoreRegisterReq)

        //then
        assertThat(response.token).isEqualTo(registerBefore.token)
    }

    @Test
    fun `대기열 등록 성공 - 대기열에 자리가 없는 경우 WAITING `(){
        //given & when
        for (i in 1..60) {
            val user = userManager.save(User("user${i}", 0))
            val req = WaitlistRequest.Register(user.id)
            waitlistRegisterUseCase(req)
        }

        //then
        val all = waitListRepository.findAll()
        val availables = all.filter { it.status == Waitlist.Status.AVAILABLE }
        val waitings = all.filter { it.status == Waitlist.Status.WAITING }
        assertThat(all.size).isEqualTo(60)
        assertThat(availables.size).isEqualTo(50)
        assertThat(waitings.size).isEqualTo(10)
    }

}
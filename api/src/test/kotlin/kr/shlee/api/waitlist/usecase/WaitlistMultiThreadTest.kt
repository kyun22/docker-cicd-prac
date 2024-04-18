package kr.shlee.api.waitlist.usecase

import kr.shlee.api.waitlist.dto.WaitlistRequest
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import kr.shlee.domain.waitlist.repository.WaitListRepository
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import kotlin.test.Test

@SpringBootTest
class WaitlistMultiThreadTest {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)
    @Autowired lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase
    @Autowired lateinit var userManager: UserManager
    @Autowired lateinit var waitListRepository: WaitListRepository

    @Test
    fun `한명의 유저가 동시에 여러번 요청해도 하나의 토큰만 발급`(){
        val user = userManager.save(User("user1", 0))
        val request = WaitlistRequest.Register(user.id)

        CompletableFuture.allOf(
            CompletableFuture.runAsync { waitlistRegisterUseCase(request)},
            CompletableFuture.runAsync { waitlistRegisterUseCase(request)},
            CompletableFuture.runAsync { waitlistRegisterUseCase(request)},
            CompletableFuture.runAsync { waitlistRegisterUseCase(request)},
            CompletableFuture.runAsync { waitlistRegisterUseCase(request)},
        ).join()

        val waitlist = waitListRepository.findByUserId("user1")
        assertThat(waitlist).isNotNull
        assertThat(waitlist?.userId).isEqualTo("user1")
    }

    @Test
    fun `동시에 다수의 유저가 요청해도 정상적으로 동작한다`(){
        val user1 = userManager.save(User("user1", 0))
        val user2 = userManager.save(User("user2", 0))
        val user3 = userManager.save(User("user3", 0))
        val user4 = userManager.save(User("user4", 0))
        val user5 = userManager.save(User("user5", 0))
        val user6 = userManager.save(User("user6", 0))
        val user7 = userManager.save(User("user7", 0))
        val user8 = userManager.save(User("user8", 0))
        val user9 = userManager.save(User("user9", 0))
        val user10 = userManager.save(User("user10", 0))
        val user11 = userManager.save(User("user11", 0))
        val user12 = userManager.save(User("user12", 0))
        val user13 = userManager.save(User("user13", 0))
        val user14 = userManager.save(User("user14", 0))
        val user15 = userManager.save(User("user15", 0))
        val user16 = userManager.save(User("user16", 0))
        val user17 = userManager.save(User("user17", 0))
        val user18 = userManager.save(User("user18", 0))
        val user19 = userManager.save(User("user19", 0))
        val user20 = userManager.save(User("user20", 0))

        CompletableFuture.allOf(
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user1.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user2.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user3.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user4.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user5.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user6.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user7.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user8.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user9.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user10.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user11.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user12.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user13.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user14.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user15.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user16.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user17.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user18.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user19.id))},
            CompletableFuture.runAsync { waitlistRegisterUseCase(WaitlistRequest.Register(user20.id))},
        ).join()

        val all = waitListRepository.findAll()
        all.forEach { waitlist -> log.info(waitlist.toString()) }
        assertThat(all.size).isEqualTo(20)
    }


}
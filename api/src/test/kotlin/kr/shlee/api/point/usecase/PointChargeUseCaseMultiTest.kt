package kr.shlee.api.point.usecase

import kr.shlee.api.point.dto.PointRequest
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import kotlin.test.Test

@SpringBootTest
class PointChargeUseCaseMultiTest {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var pointChargeUseCase: PointChargeUseCase

    @Autowired
    lateinit var userManager: UserManager

    @DisplayName("point charge - 동시에 여러 요청")
    @Test
    fun `t1`() {
        val user = userManager.save(User("user1", 0))
        assertThat(user).isNotNull
        val userId = "user1"
        val request = PointRequest.Charge(userId, 1000)

        CompletableFuture.allOf(
            CompletableFuture.runAsync { pointChargeUseCase(request) },
            CompletableFuture.runAsync { pointChargeUseCase(request) },
            CompletableFuture.runAsync { pointChargeUseCase(request) },
            CompletableFuture.runAsync { pointChargeUseCase(request) },
            CompletableFuture.runAsync { pointChargeUseCase(request) }
        ).join()

        val user1 = userManager.find(userId)
        assertThat(user1?.point).isEqualTo(1000 * 5)
    }
}
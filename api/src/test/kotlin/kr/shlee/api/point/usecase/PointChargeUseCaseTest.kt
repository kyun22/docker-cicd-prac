package kr.shlee.api.point.usecase

import kr.shlee.api.point.dto.PointRequest
import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class PointChargeUseCaseTest {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var pointChargeUseCase: PointChargeUseCase

    @Autowired
    lateinit var userManager: UserManager

    @BeforeEach
    fun setUp() {
        userManager.save(User("user1", 0))
    }

    @Test
    fun `point charge 테스트 - 유저 존재 X`() {
        //given
        val request = PointRequest.Charge("user2", 10000)

        //when
        assertThrows<UserException> { pointChargeUseCase(request) }
    }

    @Test
    fun `point charge 테스트 - 유저가 존재`() {
        //given
        val request = PointRequest.Charge("user1", 10000)

        //when
        val result = pointChargeUseCase(request)!!

        //then
        assertThat(result.point).isEqualTo(10000)
        val get = userManager.get("user1")
        assertThat(result.point).isEqualTo(get.point)
    }
}
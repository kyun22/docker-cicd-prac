package kr.shlee.api.point.usecase

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class PointChargeUseCaseTest {

    @MockK
    lateinit var userManager: UserManager

    @InjectMockKs
    lateinit var pointChargeUseCase: PointChargeUseCase

    @Test
    fun `포인트 충전 유즈케이스 테스트`() {
        // given
        val req = PointRequest.Charge("user1", 10000)
        every { userManager.find("user1") } returns User.newInstance("user1")
        every { userManager.save(any(User::class)) } returns User("user1", 10000)

        // when
        val result = pointChargeUseCase.execute(req)!!

        // then
        assertThat(result.id).isEqualTo("user1")
        assertThat(result.point).isEqualTo(10000)
    }

}
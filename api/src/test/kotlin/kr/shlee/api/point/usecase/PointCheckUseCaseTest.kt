package kr.shlee.api.point.usecase

import kr.shlee.domain.common.error.UserException
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class PointCheckUseCaseTest {
    @Autowired lateinit var pointCheckUseCase: PointCheckUseCase
    @Autowired lateinit var userManager: UserManager

    @BeforeEach
    fun setUP() {
        userManager.save(User("user1" , 1000))
    }


    @Test
    fun `포인트 체크 테스트`() {
        val user = pointCheckUseCase("user1")
        assertThat(user.id).isEqualTo("user1")
        assertThat(user.point).isEqualTo(1000)

    }

    @Test
    fun `포인트 체크 실패 - 존재하지 않는 유저`(){
        val throws = assertThrows<UserException> { pointCheckUseCase("user2") }
        assertThat(throws.errorResult).isEqualTo(UserException.UserErrorResult.USER_NOT_FOUND)
    }


}
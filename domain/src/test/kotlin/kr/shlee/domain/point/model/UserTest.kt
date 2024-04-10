package kr.shlee.domain.point.model

import kr.shlee.domain.common.error.UserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class UserTest {
    @Test
    fun `포인트 충전 성공`(){
        val user = User("user1", 0)
        val userAfterAddPoint = user.apply { addPoint(1000) }
        assertThat(userAfterAddPoint.id).isEqualTo("user1")
        assertThat(userAfterAddPoint.point).isEqualTo(1000)
    }

    @Test
    fun `포인트 차감 실패 - 잔액이 부족함`(){
        val user = User("user1", 0)
        val thrown = assertThrows<UserException> { user.subtractPoint(1000) }
        assertThat(thrown.errorResult.message).isEqualTo(UserException.UserErrorResult.LACK_OF_POINT.message)
    }

    @Test
    fun `new instance 테스트`(){
        val newInstance = User.newInstance("user1")
        assertThat(newInstance.id).isEqualTo("user1")
        assertThat(newInstance.point).isEqualTo(0)
    }

}
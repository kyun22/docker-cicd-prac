package kr.shlee.domain.point.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import kr.shlee.domain.common.error.UserException

@Entity
@Table(name = "member")
class User(
    @Id
    val id: String,
    var point: Int,
    @Version
    var version: Long? = null
) {

    fun addPoint(amount: Int) {
        this.point += amount
    }

    fun subtractPoint(amount: Int) {
        if (point < amount) throw UserException(UserException.UserErrorResult.LACK_OF_POINT)
        this.point -= amount
    }

    fun isEnoughPoint(amount: Int) : Boolean {
        return point >= amount
    }

    companion object {
        fun newInstance(userId: String): User {
            return User(userId, 0)
        }

    }
}
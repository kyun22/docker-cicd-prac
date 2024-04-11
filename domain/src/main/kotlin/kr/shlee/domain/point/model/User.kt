package kr.shlee.domain.point.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import kr.shlee.domain.common.error.UserException

@Entity
class User(
    @Id @GeneratedValue
    val id: String,
    var point: Int
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
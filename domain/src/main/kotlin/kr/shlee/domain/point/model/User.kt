package kr.shlee.domain.point.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class User(
    @Id @GeneratedValue
    val id: String?,
    var point: Int
) {

    fun addPoint(amount: Int) {
        this.point += amount
    }

    fun subtractPoint(amount: Int) {
        this.point -= amount
    }

    companion object {
        fun newInstance(): User {
            return User(null, 0)
        }

    }
}
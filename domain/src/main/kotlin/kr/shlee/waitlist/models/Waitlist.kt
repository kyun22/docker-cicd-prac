package kr.shlee.waitlist.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
class Waitlist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val token: String,
    val userId: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime?,
    var status: Status
) {
    enum class Status {
        AVAILABLE, WAITING, EXPIRED,
        ;

    }

    companion object {
        fun newOf(userId: String): Waitlist {
            return Waitlist(
                id = null,
                token = UUID.randomUUID().toString(),
                userId = userId,
                createdAt = LocalDateTime.now(),
                expiredAt = null,
                status = Status.WAITING
            )
        }
    }

    fun changeStatus(status: Status) {
        this.status = status
    }

    fun getPositionFromLastWaitlist(lastWaitlist: Waitlist): Long {
        return lastWaitlist.id!!.minus(this.id!!)
    }
}


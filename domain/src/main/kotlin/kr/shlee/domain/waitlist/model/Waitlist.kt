package kr.shlee.domain.waitlist.model

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
    var updateStatusAt: LocalDateTime?,
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
                updateStatusAt = null,
                status = Status.WAITING
            )
        }
    }

    fun changeStatus(status: Status) {
        this.status = status
        this.updateStatusAt = LocalDateTime.now()
    }

    fun getPositionFromLastWaitlist(lastWaitlist: Waitlist): Long {
        return this.id!!.minus(lastWaitlist.id!!)
    }
}


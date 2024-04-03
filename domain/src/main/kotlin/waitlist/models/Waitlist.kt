package waitlist.models

import java.time.LocalDateTime

class Waitlist(
    val id: String,
    val userId: String,
    val eventId: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime?,
    val status: Status
) {
    enum class Status {
        AVAILABLE, WAITING, EXPIRED,
        ;

    }

    companion object {
        fun newOf(userId: String, eventId: String): Waitlist {
            return Waitlist(
                id = "",
                userId = userId,
                eventId = eventId,
                createdAt = LocalDateTime.now(),
                expiredAt = null,
                status = Status.WAITING
            )
        }
    }
}


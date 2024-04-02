package waitlist.dto

import waitlist.models.Waitlist
import waitlist.models.WaitlistStatus

data class WaitlistResponse(
    val token: String,
    val userId: String,
    val eventId: String,
    val position: Int,
    val status: WaitlistStatus
) {
    companion object {
        fun of(waitlist: Waitlist): WaitlistResponse {
            return WaitlistResponse(
                token = waitlist.id,
                userId = waitlist.userId,
                eventId = waitlist.eventId,
                status = waitlist.status,
                position = 0
            )
        }
    }

}


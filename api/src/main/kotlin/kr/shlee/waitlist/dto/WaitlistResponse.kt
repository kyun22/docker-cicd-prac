package kr.shlee.kr.shlee.waitlist.dto

import kr.shlee.waitlist.models.Waitlist

data class WaitlistResponse(
    val token: String,
    val userId: String,
    val eventId: String,
    val position: Int,
    val status: Waitlist.Status
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


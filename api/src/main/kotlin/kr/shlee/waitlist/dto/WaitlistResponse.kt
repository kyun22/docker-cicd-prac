package kr.shlee.waitlist.dto

import kr.shlee.waitlist.models.Waitlist

data class WaitlistResponse(
    val token: String,
    val userId: String,
    val no: Long?,
    val status: Waitlist.Status
) {
    companion object {
        fun of(waitlist: Waitlist): WaitlistResponse {
            return WaitlistResponse(
                token = waitlist.token,
                userId = waitlist.userId,
                status = waitlist.status,
                no = waitlist.id
            )
        }
    }

}


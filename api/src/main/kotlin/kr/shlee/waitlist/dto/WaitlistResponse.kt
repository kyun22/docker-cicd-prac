package kr.shlee.waitlist.dto

import kr.shlee.waitlist.model.Waitlist

class WaitlistResponse {
    data class Register(
        val token: String,
        val userId: String,
        val no: Long?,
        val status: Waitlist.Status
    ){
        companion object {
            fun of(waitlist: Waitlist): WaitlistResponse.Register {
                return WaitlistResponse.Register(
                    token = waitlist.token,
                    userId = waitlist.userId,
                    status = waitlist.status,
                    no = waitlist.id
                )
            }
        }
    }

    data class Position(
        val position: Long
    )

}


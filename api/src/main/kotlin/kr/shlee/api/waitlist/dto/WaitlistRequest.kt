package kr.shlee.api.waitlist.dto

class WaitlistRequest{
    data class Register(
        val userId: String,
    )

    data class Position(
        val token: String,
    )
}
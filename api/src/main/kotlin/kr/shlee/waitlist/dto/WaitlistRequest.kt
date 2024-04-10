package kr.shlee.waitlist.dto

class WaitlistRequest{
    data class Register(
        val userId: String,
    )

    data class Position(
        val userId: String,
    )
}
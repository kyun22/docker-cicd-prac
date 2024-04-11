package kr.shlee.api.ticket.dto

class TicketRequest {
    data class Reserve(
        val userId: String,
        val eventId: String,
        val seatIds: List<String>
    )

    data class Payment (
        val userId: String,
        val ticketIds: List<String>
    )

}

package ticket.dto

class TicketRequest {
    data class Reserve(
        val userId: String,
        val eventId: String,
        val seatIds: List<Long>
    )

    data class Payment (
        val userId: String,
        val ticketIds: List<String>
    )

}

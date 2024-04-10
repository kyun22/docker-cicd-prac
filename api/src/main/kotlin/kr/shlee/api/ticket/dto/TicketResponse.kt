package kr.shlee.api.ticket.dto

import kr.shlee.domain.ticket.model.Ticket

class TicketResponse {

    class Reserve(
        val tickets: List<Ticket>,
        val userId: String,
        val quantity: Int,
        val totalPrice: Int,
        val status: Ticket.Status
    ) {
        companion object {
            fun of(tickets: List<Ticket>): Reserve {
                return Reserve(
                    tickets = tickets,
                    userId = tickets.first().userId,
                    quantity = tickets.size,
                    totalPrice = tickets.sumOf { it.seat.price },
                    status = Ticket.Status.WAITING_PAYMENT
                )
            }
        }

    }

    class Payment (
        val tickets: List<Ticket>,
        val userId: String,
        val point: Int
    ){
        companion object {
            fun of(tickets: List<Ticket>, point: Int): Payment {
                return Payment(
                    tickets = tickets,
                    userId = tickets.first().userId,
                    point = point
                )
            }
        }

    }

}

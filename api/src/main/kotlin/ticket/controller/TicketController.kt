package ticket.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ticket.usecase.TicketPaymentUseCase
import ticket.dto.TicketRequest
import ticket.usecase.TicketReserveUseCase
import ticket.dto.TicketResponse

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketReserveUseCase: TicketReserveUseCase,
    val ticketPaymentUseCase: TicketPaymentUseCase
) {

    @PostMapping("/reserve")
    fun reserve(
        @RequestBody request: TicketRequest.Reserve
    ): TicketResponse.Reserve {
        return ticketReserveUseCase.execute(request)
    }

    @PostMapping("/payments")
    fun pay(
        @RequestBody request: TicketRequest.Payment
    ): TicketResponse.Payment {
        return ticketPaymentUseCase.execute(request)
    }
}
package kr.shlee.api.ticket.controller

import kr.shlee.api.ticket.usecase.TicketPaymentUseCase
import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.usecase.TicketReserveUseCase
import kr.shlee.api.ticket.dto.TicketResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketReserveUseCase: TicketReserveUseCase,
    val ticketPaymentUseCase: TicketPaymentUseCase
) {

    @PostMapping("/reserve")
    fun reserve(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @RequestBody request: TicketRequest.Reserve
    ): TicketResponse.Reserve {
        return ticketReserveUseCase(request)
    }

    @PostMapping("/payments")
    fun pay(
        @RequestHeader("X-USER-TOKEN") token: String?,
        @RequestBody request: TicketRequest.Payment
    ): TicketResponse.Payment {
        return ticketPaymentUseCase.execute(request)
    }
}
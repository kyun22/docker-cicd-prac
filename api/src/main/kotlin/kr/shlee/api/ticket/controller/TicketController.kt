package kr.shlee.api.ticket.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kr.shlee.api.ticket.usecase.TicketPaymentUseCase
import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.usecase.TicketReserveUseCase
import kr.shlee.api.ticket.dto.TicketResponse

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
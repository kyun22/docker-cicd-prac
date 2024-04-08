package ticket

import kr.shlee.advice.ApiControllerAdvice
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kr.shlee.ticket.controller.TicketController
import kr.shlee.ticket.dto.TicketRequest
import kr.shlee.ticket.dto.TicketResponse
import kr.shlee.ticket.usecase.TicketPaymentUseCase
import kr.shlee.ticket.usecase.TicketReserveUseCase
import kr.shlee.waitlist.models.Event
import kr.shlee.waitlist.models.Seat
import kr.shlee.waitlist.models.Ticket
import java.time.LocalDateTime
import kotlin.test.Test

class TicketControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()
    private val ticketReserveUseCase: TicketReserveUseCase = mockk()
    private val ticketPaymentUseCase: TicketPaymentUseCase = mockk()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(TicketController(ticketReserveUseCase, ticketPaymentUseCase))
            .setControllerAdvice(ApiControllerAdvice()).build()
    }

    @Test
    fun `자리 예약 API 테스트`() {
        //given
        val request = TicketRequest.Reserve("user1", "event1", listOf(1L, 2L, 3L))
        val json = objectMapper.writeValueAsString(request)
        val event = Event("event1", "이벤트1", "서울", LocalDateTime.now(), mutableListOf())
        val seat1 = Seat(1L, event, "1", 10000, Seat.Status.AVAILABLE)
        val seat2 = Seat(2L, event, "2", 10000, Seat.Status.AVAILABLE)
        val seat3 = Seat(3L, event, "3", 10000, Seat.Status.AVAILABLE)
        val tickets = mutableListOf<Ticket>()
        tickets.add(Ticket("ticket1", "user1", seat1, Ticket.Status.WAITING_PAYMENT))
        tickets.add(Ticket("ticket2", "user1", seat2, Ticket.Status.WAITING_PAYMENT))
        tickets.add(Ticket("ticket3", "user1", seat3, Ticket.Status.WAITING_PAYMENT))
        every { ticketReserveUseCase.execute(request) } returns TicketResponse.Reserve(
            tickets,
            tickets.first().userId,
            tickets.size,
            tickets.sumOf { it.seat.price },
            Ticket.Status.WAITING_PAYMENT
        )

        //when
        mockMvc.perform(
            post("/tickets/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("tickets.length()").value(3))
            .andDo(print())

    }

    @Test
    fun `결재하기 API 테스트`() {
        //given
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        val request = TicketRequest.Payment("user1", ticketIds)
        val json = objectMapper.writeValueAsString(request)
        val event = Event("event1", "이벤트1", "서울", LocalDateTime.now(), mutableListOf())
        val seat1 = Seat(1L, event, "1", 10000, Seat.Status.PURCHASED)
        val seat2 = Seat(2L, event, "2", 10000, Seat.Status.PURCHASED)
        val seat3 = Seat(3L, event, "3", 10000, Seat.Status.PURCHASED)
        val tickets = mutableListOf<Ticket>()
        tickets.add(Ticket("ticket1", "user1", seat1, Ticket.Status.COMPLETE_PAYMENT))
        tickets.add(Ticket("ticket2", "user1", seat2, Ticket.Status.COMPLETE_PAYMENT))
        tickets.add(Ticket("ticket3", "user1", seat3, Ticket.Status.COMPLETE_PAYMENT))
        every { ticketPaymentUseCase.execute(request) } returns TicketResponse.Payment.of(tickets, 0)

        //when
        mockMvc.perform(
            post("/tickets/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("tickets.length()").value(3))
            .andDo(print())

    }

}
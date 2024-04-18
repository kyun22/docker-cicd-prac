package kr.shlee.api.ticket

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import kr.shlee.api.config.advice.ApiControllerAdvice
import kr.shlee.api.ticket.controller.TicketController
import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.api.ticket.dto.TicketResponse
import kr.shlee.api.ticket.usecase.TicketPaymentUseCase
import kr.shlee.api.ticket.usecase.TicketReserveUseCase
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDate
import kotlin.test.Test

@ExtendWith(RestDocumentationExtension::class)
class TicketControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()
    private val ticketReserveUseCase: TicketReserveUseCase = mockk()
    private val ticketPaymentUseCase: TicketPaymentUseCase = mockk()

    @BeforeEach
    fun setUp(restDocumentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.standaloneSetup(
            TicketController(ticketReserveUseCase, ticketPaymentUseCase))
            .setControllerAdvice(ApiControllerAdvice())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build()
    }

    @Test
    fun `자리 예약 API 테스트`() {
        //given
        val request = TicketRequest.Reserve("user1", "event1", listOf("1L", "2L", "3L"))
        val json = objectMapper.writeValueAsString(request)
        val event = Event("event1", "서울", LocalDate.now(), Concert("concert1", "콘서트1", "아이유"))
        val seat1 = Seat("1", event, "1", 10000, null, Seat.Status.AVAILABLE)
        val seat2 = Seat("2", event, "2", 10000, null, Seat.Status.AVAILABLE)
        val seat3 = Seat("3", event, "3", 10000, null, Seat.Status.AVAILABLE)
        val tickets = mutableListOf<Ticket>()
        tickets.add(Ticket("ticket1", User("user1", 0), seat1, Ticket.Status.WAITING_PAYMENT))
        tickets.add(Ticket("ticket2", User("user1", 0), seat2, Ticket.Status.WAITING_PAYMENT))
        tickets.add(Ticket("ticket3", User("user1", 0), seat3, Ticket.Status.WAITING_PAYMENT))
        every { ticketReserveUseCase(request) } returns TicketResponse.Reserve(
            tickets,
            tickets.first().user.id,
            tickets.size,
            tickets.sumOf { it.seat.price },
            Ticket.Status.WAITING_PAYMENT
        )

        //when
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/tickets/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("tickets.length()").value(3))
            .andDo(MockMvcRestDocumentationWrapper.document("reservation-seats"))

    }

    @Test
    fun `결재하기 API 테스트`() {
        //given
        val ticketIds = listOf("ticket1", "ticket2", "ticket3")
        val request = TicketRequest.Payment("user1", ticketIds)
        val json = objectMapper.writeValueAsString(request)
        val event = Event("event1", "서울", LocalDate.now(), Concert("concert1", "콘서트1", "아이유"))
        val seat1 = Seat("1L", event, "1", 10000, null, Seat.Status.PURCHASED)
        val seat2 = Seat("2L", event, "2", 10000, null, Seat.Status.PURCHASED)
        val seat3 = Seat("3L", event, "3", 10000, null, Seat.Status.PURCHASED)
        val tickets = mutableListOf<Ticket>()
        tickets.add(Ticket("ticket1", User("user1", 0), seat1, Ticket.Status.COMPLETE_PAYMENT))
        tickets.add(Ticket("ticket2", User("user1", 0), seat2, Ticket.Status.COMPLETE_PAYMENT))
        tickets.add(Ticket("ticket3", User("user1", 0), seat3, Ticket.Status.COMPLETE_PAYMENT))
        every { ticketPaymentUseCase(request) } returns TicketResponse.Payment.of(tickets)

        //when
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/tickets/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("tickets.length()").value(3))
            .andDo(MockMvcRestDocumentationWrapper.document("payment-tickets"))

    }

}
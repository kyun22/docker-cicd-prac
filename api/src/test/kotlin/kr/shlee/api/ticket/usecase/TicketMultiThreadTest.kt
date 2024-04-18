package kr.shlee.api.ticket.usecase

import kr.shlee.api.ticket.dto.TicketRequest
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.repository.EventRepository
import kr.shlee.domain.point.component.UserManager
import kr.shlee.domain.point.model.User
import kr.shlee.domain.ticket.component.TicketManager
import kr.shlee.domain.ticket.model.Concert
import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.model.Ticket
import kr.shlee.domain.ticket.repository.SeatRepository
import kr.shlee.domain.ticket.repository.TicketRepository
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.CannotAcquireLockException
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.CompletableFuture
import kotlin.test.Test

@SpringBootTest
class TicketMultiThreadTest {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var ticketReserveUseCase: TicketReserveUseCase

    @Autowired
    lateinit var ticketPaymentUseCase: TicketPaymentUseCase

    @Autowired
    lateinit var ticketRepository: TicketRepository

    @Autowired
    lateinit var userManager: UserManager

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Test
    fun `동시에 같은 좌석을 예약하려고 할 때 한번만 예약된다 - 한명의 유저`() {
        //given
        val user = userManager.save(User("user1", 0))
        val event = eventRepository.save(Event("event1", "seoul", LocalDate.now(), Concert("concert1", "콘서트", "가수")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 10000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 20000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 30000, null, Seat.Status.AVAILABLE))
        val request = TicketRequest.Reserve(user.id, event.id, listOf(seat1.id, seat2.id, seat3.id))

        //when
        CompletableFuture.allOf(
            CompletableFuture.runAsync { ticketReserveUseCase(request) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request) }.exceptionally { e -> log.error("Error occurred", e); null },
        ).join()

        //then
        val tickets = ticketRepository.findAll()
        tickets.forEach { ticket -> log.info(ticket.toString()) }
        val seats = tickets.map { ticket -> ticket.seat }

        assertThat(tickets.size).isEqualTo(3)
        assertThat(tickets.all { ticket -> ticket.status == Ticket.Status.WAITING_PAYMENT })
        assertThat(seats.all { seat -> seat.status == Seat.Status.RESERVED })
    }

    @Test
    fun `동시에 같은 좌석을 예약하려고 할 때 한명의 유저에게 예약된다 - 복수의 유저 동일한 좌석`(){
        //given
        val user1 = userManager.save(User("user1", 0))
        val user2 = userManager.save(User("user2", 0))
        val user3 = userManager.save(User("user3", 0))
        val user4 = userManager.save(User("user4", 0))
        val user5 = userManager.save(User("user5", 0))
        val event = eventRepository.save(Event("event1", "seoul", LocalDate.now(), Concert("concert1", "콘서트", "가수")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 10000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 20000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 30000, null, Seat.Status.AVAILABLE))
        val request1 = TicketRequest.Reserve(user1.id, event.id, listOf(seat1.id, seat2.id, seat3.id))
        val request2 = TicketRequest.Reserve(user2.id, event.id, listOf(seat1.id, seat2.id, seat3.id))
        val request3 = TicketRequest.Reserve(user3.id, event.id, listOf(seat1.id, seat2.id, seat3.id))
        val request4 = TicketRequest.Reserve(user4.id, event.id, listOf(seat1.id, seat2.id, seat3.id))
        val request5 = TicketRequest.Reserve(user5.id, event.id, listOf(seat1.id, seat2.id, seat3.id))

        //when
        CompletableFuture.allOf(
            CompletableFuture.runAsync { ticketReserveUseCase(request1) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request2) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request3) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request4) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketReserveUseCase(request5) }.exceptionally { e -> log.error("Error occurred", e); null },
        ).join()

        val tickets = ticketRepository.findAll()
        tickets.forEach { ticket -> log.info(ticket.toString()) }
        val seats = tickets.map { ticket -> ticket.seat }

        assertThat(tickets.size).isEqualTo(3)
        assertThat(tickets.all { ticket -> ticket.status == Ticket.Status.WAITING_PAYMENT })
        assertThat(seats.all { seat -> seat.status == Seat.Status.RESERVED })
    }

    @Test
    fun `같은 유저가 동시에 여러먼 결제요청해도 한번만 결제된다 `(){
        //given
        val user = userManager.save(User("user1", 100000))
        val event = eventRepository.save(Event("event1", "seoul", LocalDate.now(), Concert("concert1", "콘서트", "가수")))
        val seat1 = seatRepository.save(Seat("seat1", event, "1", 1000, null, Seat.Status.AVAILABLE))
        val seat2 = seatRepository.save(Seat("seat2", event, "2", 2000, null, Seat.Status.AVAILABLE))
        val seat3 = seatRepository.save(Seat("seat3", event, "3", 3000, null, Seat.Status.AVAILABLE))
        val reserveRequest = TicketRequest.Reserve(user.id, event.id, listOf(seat1.id, seat2.id, seat3.id))
        val reserveResponse = ticketReserveUseCase(reserveRequest)

        val paymentRequest = TicketRequest.Payment(user.id, reserveResponse.tickets.map { ticket -> ticket.id })

        CompletableFuture.allOf(
            CompletableFuture.runAsync { ticketPaymentUseCase(paymentRequest) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketPaymentUseCase(paymentRequest) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketPaymentUseCase(paymentRequest) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketPaymentUseCase(paymentRequest) }.exceptionally { e -> log.error("Error occurred", e); null },
            CompletableFuture.runAsync { ticketPaymentUseCase(paymentRequest) }.exceptionally { e -> log.error("Error occurred", e); null },
        ).join()

        val afterUser = userManager.get("user1")

        val tickets = ticketRepository.findAll()
        tickets.forEach { ticket -> log.info(ticket.toString()) }
        val seats = tickets.map { ticket -> ticket.seat }

        assertThat(tickets.size).isEqualTo(3)
        assertThat(tickets.all { ticket -> ticket.status == Ticket.Status.COMPLETE_PAYMENT })
        assertThat(seats.all { seat -> seat.status == Seat.Status.PURCHASED })

        assertThat(afterUser.point).isEqualTo(100000 - seat1.price - seat2.price - seat3.price)
    }

}
package kr.shlee.domain.ticket.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.shlee.domain.ticket.model.QTicket
import kr.shlee.domain.ticket.model.Ticket
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TicketCustomRepository (
    entityManager: EntityManager
) {
    private val query = JPAQueryFactory(entityManager)
    private val ticket = QTicket.ticket

    fun findAllByIds(ticketIds: List<String>): List<Ticket> {
        return query.selectFrom(ticket)
            .where(ticket.id.`in`(ticketIds)).fetch()
    }

    fun findAllReservedAndNotPaidTickets(): List<Ticket>? {
        return query.selectFrom(ticket)
            .where(ticket.status.eq(Ticket.Status.WAITING_PAYMENT)
                .and(ticket.createdAt.before(LocalDateTime.now().minusMinutes(5))))
            .fetch()
    }


}
package kr.shlee.domain.ticket.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.shlee.domain.ticket.model.QTicket
import kr.shlee.domain.ticket.model.Ticket
import org.springframework.stereotype.Repository

@Repository
class TicketCustomRepository (
    entityManager: EntityManager
) {
    private val query = JPAQueryFactory(entityManager)
    private val ticket = QTicket.ticket

    fun findAllByIds(ticketIds: List<String>): List<Ticket>? {
        return query.selectFrom(ticket)
            .where(ticket.id.`in`(ticketIds)).fetch()
    }



}
package kr.shlee.domain.ticket.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import kr.shlee.domain.ticket.model.QSeat
import kr.shlee.domain.ticket.model.Seat
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SeatCustomRepository(
    entityManager: EntityManager
) {
    private val query = JPAQueryFactory(entityManager)
    private val seat = QSeat.seat

    @Transactional
    fun findSeatByIds(seatIds: List<String>): List<Seat> {
        return query.selectFrom(seat).where(seat.id.`in`(seatIds))
            .setLockMode(LockModeType.OPTIMISTIC)
            .fetch()
    }

    fun findAllByEventId(eventId: String): List<Seat> {
        return query.selectFrom(seat).where(seat.event.id.eq(eventId)).fetch()
    }

}
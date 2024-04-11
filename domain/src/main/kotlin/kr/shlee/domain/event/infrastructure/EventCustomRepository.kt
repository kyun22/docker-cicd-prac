package kr.shlee.domain.event.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.shlee.domain.event.model.Event
import kr.shlee.domain.event.model.QEvent
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class EventCustomRepository (
    entityManager:EntityManager
){
    private val query = JPAQueryFactory(entityManager)
    private val event = QEvent.event

    fun findByDate(date: LocalDate): List<Event> {
        return query.selectFrom(event)
            .where(event.date.eq(date)).fetch()
    }

}
package kr.shlee.domain.waitlist.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.shlee.domain.waitlist.model.QWaitlist
import kr.shlee.domain.waitlist.model.Waitlist
import org.springframework.stereotype.Repository

@Repository
class WaitlistCustomRepository (
    entityManager: EntityManager
){
    private var query = JPAQueryFactory(entityManager)
    private val waitlist = QWaitlist.waitlist

    fun getLastAvailableWaitlist(): Waitlist? {
        return query
            .select(waitlist)
            .from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .orderBy(waitlist.createdAt.desc())
            .fetchFirst()
    }

    fun getAvailableCount(): Long {
        return query.select().from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .fetchCount()
    }

    fun getFirstWaitingWaitlist(): Waitlist? {
        return query.selectFrom(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.WAITING))
            .orderBy(waitlist.createdAt.asc())
            .fetchFirst()
    }

}


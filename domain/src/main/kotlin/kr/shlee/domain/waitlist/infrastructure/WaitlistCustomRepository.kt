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
    var query = JPAQueryFactory(entityManager)

    fun getLastAvailableWaitlist(): Waitlist? {
        val waitlist = QWaitlist.waitlist
        return query
            .select(waitlist)
            .from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .orderBy(waitlist.createdAt.desc())
            .fetchFirst()
    }

    fun getAvailableCount(): Long {
        val waitlist = QWaitlist.waitlist
        return query.select().from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .fetchCount()
    }

}


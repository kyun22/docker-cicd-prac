package kr.shlee.waitlist.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.shlee.waitlist.model.QWaitlist
import kr.shlee.waitlist.model.Waitlist
import org.springframework.stereotype.Repository

@Repository
class WaitlistCustomRepository {
    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun getLastAvailableWaitlist(): Waitlist? {
        // todo: JpaQueryFactory 클래스멤버로?
        val query = JPAQueryFactory(entityManager)
        val waitlist = QWaitlist.waitlist
        return query
            .select(waitlist)
            .from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .orderBy(waitlist.createdAt.desc())
            .fetchFirst()
    }

    fun getAvailableCount(): Long {
        val query = JPAQueryFactory(entityManager)
        val waitlist = QWaitlist.waitlist
        return query.select().from(waitlist)
            .where(waitlist.status.eq(Waitlist.Status.AVAILABLE))
            .fetchCount()
    }

}


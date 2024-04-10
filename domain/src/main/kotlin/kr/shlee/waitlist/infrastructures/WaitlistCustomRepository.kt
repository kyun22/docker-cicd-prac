package kr.shlee.waitlist.infrastructures

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.shlee.waitlist.models.QWaitlist
import kr.shlee.waitlist.models.Waitlist
import org.springframework.stereotype.Repository

@Repository
class WaitlistCustomRepository {
    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun getLastAvailableWaitlist(): Waitlist? {
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


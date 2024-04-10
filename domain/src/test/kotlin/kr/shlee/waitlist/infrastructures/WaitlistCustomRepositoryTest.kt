package kr.shlee.waitlist.infrastructures

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import kr.shlee.waitlist.models.QWaitlist
import kr.shlee.waitlist.models.Waitlist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
@Transactional
class WaitlistCustomRepositoryTest {
    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var waitlistCustomRepository: WaitlistCustomRepository

    @BeforeEach
    fun setUp() {
        val w1 = Waitlist.newOf("user1").apply { changeStatus(Waitlist.Status.AVAILABLE) }
        val w2 = Waitlist.newOf("user2").apply { changeStatus(Waitlist.Status.AVAILABLE) }
        val w3 = Waitlist.newOf("user3").apply { changeStatus(Waitlist.Status.AVAILABLE) }
        val w4 = Waitlist.newOf("user4").apply { changeStatus(Waitlist.Status.WAITING) }
        val w5 = Waitlist.newOf("user5").apply { changeStatus(Waitlist.Status.WAITING) }

        entityManager.persist(w1)
        entityManager.persist(w2)
        entityManager.persist(w3)
        entityManager.persist(w4)
        entityManager.persist(w5)
    }

    @Test
    fun `querydsl 테스트 - select`(){
        val query = JPAQueryFactory(entityManager)
        val waitlist = QWaitlist.waitlist

        val result = query.select(waitlist)
            .from(waitlist).fetch()

        assertThat(result.size).isEqualTo(5)
        assertThat(result.get(0).userId).isEqualTo("user1")
    }

    @Test
    fun `마지막 waitlist 가져오기 `(){
        val lastAvailableWaitlist = waitlistCustomRepository.getLastAvailableWaitlist()
        assertThat(lastAvailableWaitlist?.userId).isEqualTo("user3")
    }


}
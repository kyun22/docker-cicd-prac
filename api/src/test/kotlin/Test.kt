import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class Test {
    @MockK
    private lateinit var eventRepository: WaitListRepository

    @MockK
    private lateinit var waitListRepository: WaitListRepository

    @InjectMockKs
    private lateinit var waitlistRegisterUseCase: WaitlistRegisterUseCase

    @Test
    fun `userId로 토큰을 발급한다`() {
        val request: TokenRequestDto = TokenRequestDto("user1", "event1")
        val response = waitlistRegisterUseCase.execute(request)

        assertThat(response.userId).isEqualTo("user1")
        assertThat(response.eventId).isEqualTo("event1")
        assertThat(response.status).isEqualTo(WaitlistStatus.WAITING)

    }

}

class WaitlistRegisterUseCase(
    private val eventRepository: EventRepository,
    val waitlistRepository: WaitListRepository
) {

    fun execute(request: TokenRequestDto): WaitlistResponse {
        // event가 존재하는지 체크
        val event = eventRepository.findById(request.eventId) ?: throw RuntimeException("존재하지 않는 이벤트")
        // event가 존재하지 않는다면 exception

        // 이미 등록한 대기열이 있는지 체크
        val before = waitlistRepository.findByUserIdAndEventId(request.userId, request.eventId)
        // todo, 이미 존재하는 경우 처리해야함 ?.
        //  이미 발급된 토큰을 만료시간 갱신해서 주기

        // 대기열 순번 등록
        val waitlist = waitlistRepository.save(Waitlist.newOf(request.userId, event.id))
        // 상태 체크 : available, waiting
        // 대기열 순번 생성
        // 토큰 발급
        // 응답

        return WaitlistResponse.of(waitlist)
    }

}

data class WaitlistResponse(
    val token: String,
    val userId: String,
    val eventId: String,
    val position: Int,
    val status: WaitlistStatus
) {
    companion object {
        fun of(waitlist: Waitlist): WaitlistResponse {
            return WaitlistResponse(
                token = waitlist.id,
                userId = waitlist.userId,
                eventId = waitlist.eventId,
                status = waitlist.status,
                position = 0
            )
        }
    }

}

interface WaitListRepository {
    fun findByUserIdAndEventId(userId: String, eventId: String): Waitlist
    fun save(waitlist: Waitlist): Waitlist
}

enum class WaitlistStatus {
    AVAILABLE, WAITING, EXPIRED,
    ;

}


class Waitlist(
    val id: String,
    val userId: String,
    val eventId: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime?,
    val status: WaitlistStatus
) {
    companion object {
        fun newOf(userId: String, eventId: String): Waitlist {
            return Waitlist(
                id = "",
                userId = userId,
                eventId = eventId,
                createdAt = LocalDateTime.now(),
                expiredAt = null,
                status = WaitlistStatus.WAITING
            )
        }
    }
}

interface EventRepository {
    abstract fun findById(eventId: String): Event
}

class Event(
    val id: String,
    val name: String,
    val location: String,
    val date: LocalDateTime
) {

}

data class TokenRequestDto(
    val userId: String,
    val eventId: String
)

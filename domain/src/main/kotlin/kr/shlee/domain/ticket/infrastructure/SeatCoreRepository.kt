package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.repository.SeatRepository
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepository(
    val seatJpaRepository: SeatJpaRepository
) : SeatRepository {
    override fun findById(id: Long): Seat? {
        return seatJpaRepository.findById(id).orElse(null)
    }
}
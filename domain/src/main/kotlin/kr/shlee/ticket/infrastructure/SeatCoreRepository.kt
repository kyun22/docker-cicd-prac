package kr.shlee.ticket.infrastructure

import kr.shlee.ticket.model.Seat
import kr.shlee.ticket.repository.SeatRepository
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepository(
    val seatJpaRepository: SeatJpaRepository
) : SeatRepository {
    override fun findById(id: Long): Seat? {
        return seatJpaRepository.findById(id).orElse(null)
    }
}
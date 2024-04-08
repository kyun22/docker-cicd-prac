package kr.shlee.ticket.infrastructures

import kr.shlee.ticket.models.Seat
import kr.shlee.ticket.repositories.SeatRepository
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepository(
    val seatJpaRepository: SeatJpaRepository
) : SeatRepository {
    override fun findById(id: Long): Seat? {
        return seatJpaRepository.findById(id).orElse(null)
    }
}
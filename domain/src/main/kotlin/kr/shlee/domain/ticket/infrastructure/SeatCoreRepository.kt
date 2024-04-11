package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Seat
import kr.shlee.domain.ticket.repository.SeatRepository
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepository(
    private val seatJpaRepository: SeatJpaRepository,
    private val seatCustomRepository: SeatCustomRepository
) : SeatRepository {
    override fun findById(id: Long): Seat? {
        return seatJpaRepository.findById(id).orElse(null)
    }

    override fun findSeats(seatIds: List<String>): List<Seat> {
        return seatCustomRepository.findSeatByIds(seatIds)
    }
}
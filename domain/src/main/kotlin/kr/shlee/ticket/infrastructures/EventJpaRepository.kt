package kr.shlee.ticket.infrastructures

import org.springframework.data.jpa.repository.JpaRepository
import kr.shlee.ticket.models.Event

interface EventJpaRepository: JpaRepository<Event, String> {
}
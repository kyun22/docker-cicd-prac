package kr.shlee.ticket.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import kr.shlee.ticket.model.Event

interface EventJpaRepository: JpaRepository<Event, String> {
}
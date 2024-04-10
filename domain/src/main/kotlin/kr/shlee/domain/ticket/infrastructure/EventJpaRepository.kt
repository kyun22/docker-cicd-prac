package kr.shlee.domain.ticket.infrastructure

import kr.shlee.domain.ticket.model.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventJpaRepository: JpaRepository<Event, String> {
}
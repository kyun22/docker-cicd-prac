package kr.shlee.domain.event.infrastructure

import kr.shlee.domain.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventJpaRepository: JpaRepository<Event, String> {
}
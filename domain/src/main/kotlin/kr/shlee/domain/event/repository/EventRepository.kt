package kr.shlee.domain.event.repository

import kr.shlee.domain.event.model.Event
import java.time.LocalDate
import java.time.LocalDateTime

interface EventRepository {
    fun findById(eventId: String): Event?
    fun findByDate(date: LocalDate): List<Event>
    fun findAll(): List<Event>
    fun save(event: Event): Event
}
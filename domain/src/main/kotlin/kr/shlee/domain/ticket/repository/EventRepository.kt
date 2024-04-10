package kr.shlee.domain.ticket.repository

import kr.shlee.domain.ticket.model.Event

interface EventRepository {
    fun findById(eventId: String): Event?
    fun findByDate(date: String): List<Event>
    fun findAll(): List<Event>
}
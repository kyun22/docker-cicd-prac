package kr.shlee.ticket.repositories

import kr.shlee.ticket.models.Event

interface EventRepository {
    fun findById(eventId: String): Event?
    fun findByDate(date: String): List<Event>
    fun findAll(): List<Event>
}
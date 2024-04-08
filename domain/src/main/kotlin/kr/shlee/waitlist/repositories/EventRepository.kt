package kr.shlee.waitlist.repositories

import kr.shlee.waitlist.models.Event

interface EventRepository {
    fun findById(eventId: String): Event?
    fun findByDate(date: String): List<Event>
    fun findByEventId(eventId: String): List<Event>
    fun findAll(): List<Event>
}
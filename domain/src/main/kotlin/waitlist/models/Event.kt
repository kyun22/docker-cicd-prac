package waitlist.models

import java.time.LocalDateTime

class Event(
    val id: String,
    val name: String,
    val location: String,
    val date: LocalDateTime
) {

}
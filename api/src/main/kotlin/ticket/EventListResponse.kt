package ticket

import waitlist.models.Event
import java.time.format.DateTimeFormatter

class EventListResponse (
    val events: List<EventResponse>
){
    companion object {
        fun newOf(events: List<Event>): MutableList<EventResponse> {
            val list = mutableListOf<EventResponse>()
            events.map { event: Event -> list.add(EventResponse(
                id = event.id,
                name = event.name,
                localtion = event.location,
                date = event.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            }
            return list
        }
    }
}

data class EventResponse (
    val id: String,
    val name: String,
    val localtion: String,
    val date : String
)
package kr.shlee.waitlist.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Concert(
    @Id @GeneratedValue
    val id: String,
    val name: String,
    val singer: String,
) {
}
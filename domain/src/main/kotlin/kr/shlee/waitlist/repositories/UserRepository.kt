package kr.shlee.waitlist.repositories

import kr.shlee.waitlist.models.User

interface UserRepository {
    fun findById(userId: String): User?
    fun save(user: User): User

}
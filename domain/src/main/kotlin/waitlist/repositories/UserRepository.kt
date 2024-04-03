package waitlist.repositories

import waitlist.models.User

interface UserRepository {
    fun findById(userId: String): User?
    fun save(user: User): User

}
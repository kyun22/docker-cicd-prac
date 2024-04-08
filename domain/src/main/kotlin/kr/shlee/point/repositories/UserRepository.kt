package kr.shlee.point.repositories

import kr.shlee.point.models.User

interface UserRepository {
    fun findById(userId: String): User?
    fun save(user: User): User

}
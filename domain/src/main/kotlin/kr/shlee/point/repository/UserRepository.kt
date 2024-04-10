package kr.shlee.point.repository

import kr.shlee.point.model.User

interface UserRepository {
    fun findById(userId: String): User?
    fun save(user: User): User

}
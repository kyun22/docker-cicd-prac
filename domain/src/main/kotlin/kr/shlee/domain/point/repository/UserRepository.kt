package kr.shlee.domain.point.repository

import kr.shlee.domain.point.model.User

interface UserRepository {
    fun findById(userId: String): User?
    fun save(user: User): User

}
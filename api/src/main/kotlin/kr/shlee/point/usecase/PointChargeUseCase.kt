package kr.shlee.kr.shlee.point.usecase

import org.springframework.stereotype.Component
import kr.shlee.kr.shlee.point.dto.PointRequest
import kr.shlee.waitlist.models.User
import kr.shlee.waitlist.repositories.UserRepository

@Component
class PointChargeUseCase(
    val userRepository: UserRepository
) {

    fun execute(request: PointRequest.Charge): User? {
        var user = userRepository.findById(request.userId) ?: User.newInstance()
        return userRepository.save(user.apply { this.addPoint(request.amount) })
    }
}


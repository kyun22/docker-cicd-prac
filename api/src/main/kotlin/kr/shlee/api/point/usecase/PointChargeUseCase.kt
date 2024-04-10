package kr.shlee.api.point.usecase

import kr.shlee.domain.point.model.User
import kr.shlee.domain.point.repository.UserRepository
import kr.shlee.api.point.dto.PointRequest
import org.springframework.stereotype.Component

@Component
class PointChargeUseCase(
    val userRepository: UserRepository
) {

    fun execute(request: PointRequest.Charge): User? {
        var user = userRepository.findById(request.userId) ?: User.newInstance()
        return userRepository.save(user.apply { this.addPoint(request.amount) })
    }
}


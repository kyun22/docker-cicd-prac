package point.usecase

import org.springframework.stereotype.Component
import point.dto.PointRequest
import waitlist.models.User
import waitlist.repositories.UserRepository

@Component
class PointChargeUseCase(
    val userRepository: UserRepository
) {

    fun execute(request: PointRequest.Charge): User? {
        var user = userRepository.findById(request.userId) ?: User.newInstance()
        return userRepository.save(user.apply { this.addPoint(request.amount) })
    }
}


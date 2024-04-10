package kr.shlee.api.point.usecase

import kr.shlee.domain.point.model.User
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.domain.point.component.UserManager
import org.springframework.stereotype.Component

@Component
class PointChargeUseCase(
    val userManager: UserManager
) {
    fun execute(request: PointRequest.Charge): User? {
        var user = userManager.find(request.userId) ?: User.newInstance(request.userId)
        return userManager.save(user.apply { addPoint(request.amount) })
    }
}


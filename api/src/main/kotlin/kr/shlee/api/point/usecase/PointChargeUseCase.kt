package kr.shlee.api.point.usecase

import kr.shlee.domain.point.model.User
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.domain.point.component.UserManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Component
class PointChargeUseCase(
    val userManager: UserManager
) {
    @Transactional
    operator fun invoke(request: PointRequest.Charge): User? {
        val user = userManager.getWithLock(request.userId)
        user.addPoint(request.amount)
        return user
    }
}


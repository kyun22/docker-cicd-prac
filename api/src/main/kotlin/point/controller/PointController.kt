package point.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import point.dto.PointRequest
import point.usecase.PointChargeUseCase
import point.usecase.PointCheckUseCase
import waitlist.models.User

@RestController
@RequestMapping("/points")
class PointController(
    val pointChargeUseCase: PointChargeUseCase,
    val pointCheckUseCase: PointCheckUseCase,
) {

    @PostMapping("/charge")
    fun charge(
        @RequestBody request: PointRequest.Charge
    ): User? {
        return pointChargeUseCase.execute(request)
    }

    @GetMapping("/{userId}")
    fun check(
        @PathVariable userId: String
    ): User {
        return pointCheckUseCase.execute(userId)
    }
}
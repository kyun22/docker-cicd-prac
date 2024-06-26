package kr.shlee.api.point.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kr.shlee.api.point.dto.PointRequest
import kr.shlee.api.point.usecase.PointChargeUseCase
import kr.shlee.api.point.usecase.PointCheckUseCase
import kr.shlee.domain.point.model.User

@RestController
@RequestMapping("/points")
class PointController(
    val pointChargeUseCase: PointChargeUseCase,
    val pointCheckUseCase: PointCheckUseCase,
) {

    @PostMapping("/{userId}/charge")
    fun charge(
        @RequestBody request: PointRequest.Charge
    ): User? {
        return pointChargeUseCase(request)
    }

    @GetMapping("/{userId}/check")
    fun check(
        @PathVariable userId: String
    ): User {
        return pointCheckUseCase(userId)
    }
}
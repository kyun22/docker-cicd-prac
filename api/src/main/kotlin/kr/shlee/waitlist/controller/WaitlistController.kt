package kr.shlee.waitlist.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kr.shlee.waitlist.dto.WaitlistRequest
import kr.shlee.waitlist.dto.WaitlistResponse
import kr.shlee.waitlist.usecase.WaitlistCheckOrderUseCase
import kr.shlee.waitlist.usecase.WaitlistRegisterUseCase

@RequestMapping("/waitlist")
@RestController
class WaitlistController(
    val registerUseCase: WaitlistRegisterUseCase,
    val checkOrderUseCase: WaitlistCheckOrderUseCase
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: WaitlistRequest.Register
    ): WaitlistResponse.Register {
        return registerUseCase.execute(request)
    }

    @GetMapping("/position")
    fun position(
        @RequestBody request: WaitlistRequest.Position
    ): WaitlistResponse.Position {
        return checkOrderUseCase.execute(request)
    }
}
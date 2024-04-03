package waitlist.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import waitlist.dto.WaitlistRequest
import waitlist.dto.WaitlistResponse
import waitlist.usecase.WaitlistCheckOrderUseCase
import waitlist.usecase.WaitlistRegisterUseCase

@RequestMapping("/waitlist")
@RestController
class WaitlistController(
    val registerUseCase: WaitlistRegisterUseCase,
    val checkOrderUseCase: WaitlistCheckOrderUseCase
) {

    @PostMapping("/")
    fun generate(
        @RequestBody waitlistRequest: WaitlistRequest
    ): WaitlistResponse {
        return registerUseCase.execute(waitlistRequest)
    }

    @GetMapping("/{userId}/{eventId}")
    fun check(
        @PathVariable userId: String,
        @PathVariable eventId: String,
    ): WaitlistResponse {
        return checkOrderUseCase.execute(userId, eventId)
    }
}
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WaitlistController(
    val registerUseCase: WaitlistRegisterUseCase
) {

    @GetMapping("/waitlist")
    fun generate (
        @RequestBody tokenRequestDto: TokenRequestDto
    ): WaitlistResponse {
        return registerUseCase.execute(tokenRequestDto)
    }

}
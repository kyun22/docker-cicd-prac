import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class MockApiTest (
    val mockMvc: MockMvc,
){
    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `토큰을 발급한다`() {
        val request: TokenRequestDto = TokenRequestDto("user1", "event1")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/waitlist")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

    }

}


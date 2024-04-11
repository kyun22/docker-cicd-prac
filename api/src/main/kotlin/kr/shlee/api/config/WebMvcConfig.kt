package kr.shlee.api.config

import kr.shlee.domain.waitlist.component.WaitlistTokenValidator
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    val tokenValidator: WaitlistTokenValidator
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(WaitlistTokenInterceptor(tokenValidator))
            .addPathPatterns("/**")
            .excludePathPatterns("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
            .excludePathPatterns("/waitlist/**")
            .excludePathPatterns("/points/**")
    }
}
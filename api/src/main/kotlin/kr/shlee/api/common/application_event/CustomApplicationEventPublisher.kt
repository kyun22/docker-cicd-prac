package kr.shlee.api.common.application_event

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CustomApplicationEventPublisher : ApplicationEventPublisher {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun publishEvent(event: Any) {
        log.info("Publish Application Event. EventType: {}", event.javaClass.toString())
        publishEvent(event)
    }

}
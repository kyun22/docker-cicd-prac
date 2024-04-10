package kr.shlee.domain.base

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("local")
open class BaseTestCase {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)
}
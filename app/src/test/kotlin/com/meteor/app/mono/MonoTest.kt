package com.meteor.app.mono

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.lang.RuntimeException
import java.time.Duration

/**
 * MonoTest
 *
 * @author unseok.kim (unseok.kim@linecorp.com)
 * @since 2021. 01. 30.
 */
class MonoTest {
    @Test
    internal fun monoNever() {
        Assertions.assertThrows(AssertionFailedError::class.java) {
            Assertions.assertTimeoutPreemptively(Duration.ofMillis(5000)) {
                //무기한 대기, signal이 발생하지 않는다.
                Mono.never<String>().block()
            }
        }
    }


}
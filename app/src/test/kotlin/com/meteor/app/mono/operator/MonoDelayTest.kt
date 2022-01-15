package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class MonoDelayTest {
    @Test
    internal fun delayElementTest() {
        val value = "Hello"
        val block = Mono.just(value).delayElement(Duration.ofMillis(300))
        StepVerifier.create(block)
            .verifyTimeout(Duration.ofMillis(200))

        val empty = Mono.empty<String>().delayElement(Duration.ofMillis(300))
        StepVerifier.create(empty)
            .verifyComplete()
    }

    @Test
    internal fun delayUntilTest() {
        val value = "Hello"
        val block = Mono.just(value).delayUntil {
            val justValue = it
            Mono.delay(Duration.ofMillis(300)).map {
                justValue
            }
        }
        StepVerifier.create(block)
            .verifyTimeout(Duration.ofMillis(200))

        val empty = Mono.empty<String>().delayUntil {
            val justValue = it
            Mono.delay(Duration.ofMillis(300)).map {
                justValue
            }
        }
        StepVerifier.create(empty)
            .verifyComplete()
    }

    @Test
    internal fun delaySubscriptionTest() {
        val value = "Hello"
        val block = Mono.just(value).delaySubscription(Duration.ofMillis(300))
        StepVerifier.create(block)
            .verifyTimeout(Duration.ofMillis(200))

        val empty = Mono.empty<String>().delaySubscription(Duration.ofMillis(300))
        StepVerifier.create(empty)
            .verifyComplete()
    }
}
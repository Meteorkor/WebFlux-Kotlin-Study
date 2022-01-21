package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class MonoTimeTest {
    @Test
    internal fun timedTest() {
        val value = "value"
        val timedMono = Mono.just(value).timed()
            .map {
                //it.elapsed(), PT0.001900013S
                it.get()
            }

        StepVerifier.create(timedMono)
            .expectNext(value)
            .verifyComplete()
    }

    @Test
    internal fun timeoutTest() {
        val value = "value"
        val timedMono = Mono.just(value)
            .map {
                it
            }.timeout(Duration.ofMillis(500))

        StepVerifier.create(timedMono)
            .expectNext(value)
            .verifyComplete()
    }

    @Test
    internal fun beforeTimeoutTest() {
        val value = "value"
        val timedMono = Mono.empty<String>().timeout(Duration.ofMillis(100))
            .map {
                it
            }

        StepVerifier.create(timedMono)
//            .expectNext(value)
            .verifyComplete()
    }


    @Test
    internal fun timestampTest() {
        val value = "value"
        val timedMono = Mono.just(value).timestamp()
            .map {
                //it.t1, 1642774302044
                it.t2
            }

        StepVerifier.create(timedMono)
            .expectNext(value)
            .verifyComplete()
    }
}
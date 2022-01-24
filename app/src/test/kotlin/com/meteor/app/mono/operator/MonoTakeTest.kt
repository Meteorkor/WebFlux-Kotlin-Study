package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class MonoTakeTest {
    @Test
    internal fun takeTest() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                Thread.sleep(100)
                it
            }
            .take(Duration.ofMillis(1))
            .map {
                atomicInteger.incrementAndGet()
                it
            }

        StepVerifier.create(thenMono)
//            .expectNext(text)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(0)
    }

    @Test
    internal fun takeTestNotTimeout() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                it
            }
            .take(Duration.ofMillis(1000))
            .map {
                atomicInteger.incrementAndGet()
                it
            }

        StepVerifier.create(thenMono)
            .expectNext(text)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(0)
    }

    @Test
    internal fun takeUntilOtherTest() {//??
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                it
            }
            .takeUntilOther {
                println("asdas")
            }
            .map {
                atomicInteger.incrementAndGet()
                it
            }

        StepVerifier.create(thenMono)
            .expectNext(text)
//            .expectNext(text)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(0)
    }

}
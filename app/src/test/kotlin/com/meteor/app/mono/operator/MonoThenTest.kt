package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger

class MonoThenTest {
    @Test
    internal fun thenTestNotSubscribe() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                atomicInteger.incrementAndGet()
                it
            }
            .then()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(0)
    }

    @Test
    internal fun thenTest() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                atomicInteger.incrementAndGet()
                it
            }
            .then()

        StepVerifier.create(thenMono)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(1)
    }

    @Test
    internal fun thenReturnTest() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                atomicInteger.incrementAndGet()
                it + "!!!"
            }
            .thenReturn(text)

        StepVerifier.create(thenMono)
            .expectNext(text)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(1)
    }

    @Test
    internal fun thenEmptyTest() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                atomicInteger.incrementAndGet()
                it + "!!!"
            }
            .thenEmpty {
                it.onComplete()
            }

        StepVerifier.create(thenMono)
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(1)
    }

    @Test
    internal fun thenManyTest() {
        val text = "text"
        val atomicInteger = AtomicInteger(0)
        val thenMono = Mono.just(text)
            .map {
                atomicInteger.incrementAndGet()
                it + "!!!"
            }
            .thenMany(Flux.just("a", "b"))

        StepVerifier.create(thenMono)
            .expectNext("a", "b")
            .verifyComplete()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(1)
    }
}
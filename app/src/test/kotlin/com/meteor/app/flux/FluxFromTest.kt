package com.meteor.app.flux

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.stream.IntStream

class FluxFromTest {

    @Test
    internal fun fromTest() {
        val fromFlux = Flux.from(Flux.just(1, 2, 3))
        StepVerifier.create(fromFlux)
            .expectNext(1)
            .expectNext(2)
            .expectNext(3)
            .verifyComplete()
    }

    @Test
    internal fun fromStreamTest() {
        StepVerifier.create(Flux.fromStream(IntStream.range(0, 3).boxed()))
            .expectNext(0)
            .expectNext(1)
            .expectNext(2)
            .verifyComplete()
    }

    @Test
    internal fun fromArrayTest() {
        val intArr = Array(3) {
            it
        }
        StepVerifier.create(Flux.fromArray(intArr))
            .expectNext(0)
            .expectNext(1)
            .expectNext(2)
            .verifyComplete()
    }

    @Test
    internal fun fromIterableTest() {
        StepVerifier.create(Flux.fromIterable(listOf(0, 1, 2)))
            .expectNext(0)
            .expectNext(1)
            .expectNext(2)
            .verifyComplete()
    }

}
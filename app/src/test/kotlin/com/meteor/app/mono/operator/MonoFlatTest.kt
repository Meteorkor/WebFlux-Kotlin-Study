package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.stream.IntStream
import kotlin.streams.toList

class MonoFlatTest {
    @Test
    internal fun flatMapTest() {
        val num = 3
        val flatMono = Mono.just(num).flatMap {
            Mono.just(it)//실제로는 async 한 Mono
        }

        StepVerifier.create(flatMono)
            .expectNext(3)
            .verifyComplete()
    }

    @Test
    internal fun flatMapManyTest() {
        val num = 3
        val expandFlux = Mono.just(num).flatMapMany {
            Flux.range(0, it)
        }//{0,1,2}

        StepVerifier.create(expandFlux)
            .expectNext(0, 1, 2)
            .verifyComplete()
    }

    @Test
    internal fun flatMapIterableTest() {
        val num = 3
        val expandFlux = Mono.just(num).flatMapIterable {
            IntStream.range(0, num).toList()
        }//{0,1,2}

        StepVerifier.create(expandFlux)
            .expectNext(0, 1, 2)
            .verifyComplete()
    }
}
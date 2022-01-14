package com.meteor.app.mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoEtcTest {
    @Test
    internal fun ignoreElementsTest() {
        val text = "text"
        val ignoreElement = Mono.just(text)
            .ignoreElement()//emptyMono

        StepVerifier.create(ignoreElement)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest() {
        val text = "text"
        val mono1 = Mono.just(text)
        val mono2 = Mono.just(text)

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest2() {
        val text = "text"
        val mono1 = Mono.just(text)
        val mono2 = Mono.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(false)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest3() {
        val text = "text"
        val mono1 = Mono.empty<String>()
        val mono2 = Mono.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest4() {
        val text = "text"
        val flux1 = Flux.empty<String>()
        val flux2 = Flux.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest5() {
        val text = "text"
        val flux1 = Flux.just("a", "b", "c")
        val flux2 = Flux.just("a", "b", "c")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest6() {
        val text = "text"
        val flux1 = Flux.just("a", "b", "c")
        val flux2 = Flux.just("a", "b")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(false)
            .verifyComplete()
    }

    @Test
    internal fun sequenceEqualTest7() {
        val text = "text"
        val flux1 = Flux.just("a")
        val mono1 = Mono.just("a")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, mono1)
        StepVerifier.create(sequenceEqualMono)
            .expectNext(true)
            .verifyComplete()
    }
}
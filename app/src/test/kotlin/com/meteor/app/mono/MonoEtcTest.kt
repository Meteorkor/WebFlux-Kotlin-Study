package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MonoEtcTest {
    @Test
    internal fun ignoreElementsTest() {
        val text = "text"
        val ignoreElement = Mono.just(text)
            .ignoreElement()//emptyMono

        Assertions.assertThat(ignoreElement.block()).isNull()
    }

    @Test
    internal fun sequenceEqualTest() {
        val text = "text"
        val mono1 = Mono.just(text)
        val mono2 = Mono.just(text)

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(true)
    }

    @Test
    internal fun sequenceEqualTest2() {
        val text = "text"
        val mono1 = Mono.just(text)
        val mono2 = Mono.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(false)
    }

    @Test
    internal fun sequenceEqualTest3() {
        val text = "text"
        val mono1 = Mono.empty<String>()
        val mono2 = Mono.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(mono1, mono2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(true)
    }

    @Test
    internal fun sequenceEqualTest4() {
        val text = "text"
        val flux1 = Flux.empty<String>()
        val flux2 = Flux.empty<String>()

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(true)
    }

    @Test
    internal fun sequenceEqualTest5() {
        val text = "text"
        val flux1 = Flux.just("a", "b", "c")
        val flux2 = Flux.just("a", "b", "c")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(true)
    }

    @Test
    internal fun sequenceEqualTest6() {
        val text = "text"
        val flux1 = Flux.just("a", "b", "c")
        val flux2 = Flux.just("a", "b")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, flux2)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(false)
    }

    @Test
    internal fun sequenceEqualTest7() {
        val text = "text"
        val flux1 = Flux.just("a")
        val mono1 = Mono.just("a")

        val sequenceEqualMono = Mono.sequenceEqual(flux1, mono1)
        Assertions.assertThat(sequenceEqualMono.block()).isEqualTo(true)
    }
}
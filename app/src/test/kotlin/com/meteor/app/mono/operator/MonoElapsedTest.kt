package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.Duration

class MonoElapsedTest {
    @Test
    internal fun elapsedTest() {
        val text = "hello"
        val elapsedMono = Mono.just(text).elapsed().delayElement(Duration.ofMillis(500))
        val block = elapsedMono.block()!!
        Assertions.assertThat(block.t1).isLessThan(10)
    }

    @Test
    internal fun elapsed500Test() {
        val text = "hello"
        val elapsedMono = Mono.just(text).delayElement(Duration.ofMillis(500)).elapsed()
        val block = elapsedMono.block()!!
        Assertions.assertThat(block.t1).isGreaterThan(500)
    }
}
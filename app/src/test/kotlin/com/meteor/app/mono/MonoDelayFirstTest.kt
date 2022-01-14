package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class MonoDelayFirstTest {

    @Test
    internal fun delayTest() {
        val stopWatch = StopWatch()
        stopWatch.start()
        val delayMono = Mono.delay(Duration.ofSeconds(1)).map {
            println("it : ${it}")
            it
        }
        StepVerifier.create(delayMono)
            .expectNext(0)
            .verifyComplete()
        stopWatch.stop()
        Assertions.assertThat(stopWatch.totalTimeSeconds).isGreaterThan(1.0)
    }

    @Test
    internal fun firstWithSignalTest() {
        val d_100: Long = 100
        val d_300: Long = 300
        val d_700: Long = 700
        val errorMono = Mono.error<IllegalAccessException>(IllegalAccessException())
        val mono1 = Mono.delay(Duration.ofMillis(d_100)).map { d_100 }
        val mono2 = Mono.delay(Duration.ofMillis(d_300)).map { d_300 }
        val mono3 = Mono.delay(Duration.ofMillis(d_700)).map { d_700 }

        StepVerifier.create(Mono.firstWithSignal(errorMono, mono1, mono2, mono3))
            .expectError()
    }

    @Test
    internal fun firstWithSignalValueTest() {
        val d_100: Long = 100
        val d_300: Long = 300
        val d_700: Long = 700
        val errorMono = Mono.error<IllegalAccessException>(IllegalAccessException())
        val mono1 = Mono.delay(Duration.ofMillis(d_100)).map { d_100 }
        val mono2 = Mono.delay(Duration.ofMillis(d_300)).map { d_300 }
        val mono3 = Mono.delay(Duration.ofMillis(d_700)).map { d_700 }

        //ignore error signal
        val block = Mono.firstWithValue(errorMono, mono1, mono2, mono3).block()

        StepVerifier.create(Mono.firstWithValue(errorMono, mono1, mono2, mono3))
            .expectNext(d_100)
            .verifyComplete()
    }
}
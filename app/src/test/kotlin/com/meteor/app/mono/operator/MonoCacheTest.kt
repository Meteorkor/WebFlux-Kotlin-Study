package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class MonoCacheTest {
    @Test
    internal fun cacheTest() {
        val atomicInteger = AtomicInteger()

        val cacheMono = Mono.fromCallable {
            atomicInteger.incrementAndGet()
        }.cache()

        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(0)

        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
        Thread.sleep(1000)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
    }

    @Test
    internal fun cacheTtlTest() {
        val atomicInteger = AtomicInteger()

        val cacheMono = Mono.fromCallable {
            atomicInteger.incrementAndGet()
        }.cache(Duration.ofMillis(500))

        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(0)

        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
        Thread.sleep(1000)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(2)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(2)
    }

    @Test
    internal fun cacheInvalidateIfTest() {
        val atomicInteger = AtomicInteger()
        var invalidFlag = true

        val cacheMono = Mono.fromCallable {
            atomicInteger.incrementAndGet()
        }.cache(Duration.ofMillis(300))
            .cacheInvalidateWhen {
                Mono.empty()
            }
            .cacheInvalidateIf { invalidFlag }


        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(0)

        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)

        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(1)
        Thread.sleep(400)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(2)
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(2)

        Thread.sleep(400)
        invalidFlag = false
        cacheMono.block()
        Assertions.assertThat(atomicInteger.toInt()).isEqualTo(2)
    }

    @Test
    internal fun cacheInvalidateWhenTest() {
        //TODO cacheInvalidateWhen
    }
}
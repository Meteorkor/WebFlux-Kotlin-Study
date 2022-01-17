package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger

class MonoFirstFinallyTest {
    @Test
    internal fun doFirstTest() {
        val atomicInteger = AtomicInteger()
        val text = "hello"
        val doFirstMono = Mono.just(text).map {
            atomicInteger.compareAndSet(2, 3)
            text + text
        }.doFirst {
            atomicInteger.compareAndSet(1, 2)
        }.doFirst {
            atomicInteger.compareAndSet(0, 1)
        }

        StepVerifier.create(doFirstMono).expectNext(text + text).verifyComplete()
        Assertions.assertThat(atomicInteger.get()).isEqualTo(3)
    }

    @Test
    internal fun doFinallyTest() {
        val atomicInteger = AtomicInteger()
        val text = "hello"
        val doFirstMono = Mono.just(text).map {
            atomicInteger.compareAndSet(0, 1)
            text + text
        }.doFinally {
            atomicInteger.compareAndSet(2, 3)
        }.doFinally {
            atomicInteger.compareAndSet(1, 2)
        }
        StepVerifier.create(doFirstMono).expectNext(text + text).verifyComplete()
        Assertions.assertThat(atomicInteger.get()).isEqualTo(3)
    }

    @Test
    internal fun doFirstFinallyTest() {
        val atomicInteger = AtomicInteger()
        val text = "hello"
        val doFirstMono = Mono.just(text).doFirst {
            atomicInteger.compareAndSet(0, 1)

        }.doFinally {
            atomicInteger.compareAndSet(2, 3)
        }.map {
            atomicInteger.compareAndSet(1, 2)
            text + text
        }
        StepVerifier.create(doFirstMono).expectNext(text + text).verifyComplete()
        Assertions.assertThat(atomicInteger.get()).isEqualTo(3)
    }
}
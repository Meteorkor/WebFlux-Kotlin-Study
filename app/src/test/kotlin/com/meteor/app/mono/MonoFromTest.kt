package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class MonoFromTest {
    @Test
    internal fun fromCallableTest() {
        val text = "Hello"
        val callCnt = AtomicInteger()
        val callable = {
            callCnt.set(1)
            text
        }
        val fromCallable = Mono.fromCallable(callable)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(0)
        StepVerifier.create(fromCallable)
            .expectNext(text)
            .verifyComplete()
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
    }

    @Test
    internal fun fromCompletionStageTest() {
        val text = "Hello"
        val callCnt = AtomicInteger()
        val syncCheck = CountDownLatch(1)
        val minimalCompletionStage = CompletableFuture.supplyAsync {
            callCnt.set(1)
            syncCheck.countDown()
            text
        }

        Assertions.assertThat(syncCheck.await(500, TimeUnit.SECONDS)).isEqualTo(true)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)

        val fromCompletionStage = Mono.fromCompletionStage(minimalCompletionStage)
        StepVerifier.create(fromCompletionStage)
            .expectNext(text)
            .verifyComplete()
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
    }

    @Test
    internal fun fromDirectTest() {
        val text = "Hello"
        val fromDirect = Mono.fromDirect<String> {
            it.onNext(text)
            it.onComplete()
        }
        Assertions.assertThat(fromDirect.block()).isEqualTo(text)
//        StepVerifier.create(fromDirect)
//            .expectNext(text)
//            .verifyComplete()
    }

    @Test
    internal fun fromFutureTest() {
        val text = "Hello"
        val callCnt = AtomicInteger()
        val syncCheck = CountDownLatch(1)
        val minimalCompletionStage = CompletableFuture.supplyAsync {
            callCnt.set(1)
            syncCheck.countDown()
            text
        }

        Assertions.assertThat(syncCheck.await(500, TimeUnit.SECONDS)).isEqualTo(true)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)

        val fromCompletionStage = Mono.fromFuture(minimalCompletionStage)
        StepVerifier.create(fromCompletionStage)
            .expectNext(text)
            .verifyComplete()
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
    }

    @Test
    internal fun fromRunnableTest() {
        val callCnt = AtomicInteger()
        val syncCheck = CountDownLatch(1)
        val runnable = Runnable {
            callCnt.set(1)
            syncCheck.countDown()
        }

        Assertions.assertThat(syncCheck.await(1, TimeUnit.SECONDS)).isEqualTo(false)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(0)

        val fromCompletionStage = Mono.fromRunnable<String>(runnable)
        StepVerifier.create(fromCompletionStage)
            .verifyComplete()
        Assertions.assertThat(syncCheck.await(1, TimeUnit.SECONDS)).isEqualTo(true)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
    }

    @Test
    internal fun fromSupplierTest() {
        val text = "Hello"
        val callCnt = AtomicInteger()
        val syncCheck = CountDownLatch(1)
        val supplier = {
            callCnt.set(1)
            syncCheck.countDown()
            text
        }

        Assertions.assertThat(syncCheck.await(1, TimeUnit.SECONDS)).isEqualTo(false)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(0)

        val fromCompletionStage = Mono.fromSupplier(supplier)
        StepVerifier.create(fromCompletionStage)
            .expectNext(text)
            .verifyComplete()
        Assertions.assertThat(syncCheck.await(1, TimeUnit.SECONDS)).isEqualTo(true)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
        Assertions.assertThat(callCnt.toInt()).isEqualTo(1)
    }
}
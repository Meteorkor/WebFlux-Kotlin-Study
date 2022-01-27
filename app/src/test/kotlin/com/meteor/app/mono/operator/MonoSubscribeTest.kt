package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.util.concurrent.SettableListenableFuture
import reactor.core.publisher.Mono
import java.util.concurrent.CountDownLatch

class MonoSubscribeTest {
    @Test
    internal fun subscribeTest() {
        val value = "Hello"
        val latch = CountDownLatch(1)
        Mono.just(value).map {
            latch.countDown()
            it
        }.subscribe()

        latch.await()
    }

    @Test
    internal fun subscribeTestLambda() {
        val value = "Hello"
        val settableFuture = SettableListenableFuture<String>();

        Mono.just(value).map {
            it
        }.subscribe {
            settableFuture.set(it)
        }

        Assertions.assertThat(settableFuture.get()).isEqualTo(value)
    }

    @Test
    internal fun subscribeCompleteTest() {
        val value = "Hello"
        val complete = "complete"
        val settableFuture = SettableListenableFuture<String>();
        val completeFuture = SettableListenableFuture<String>();

        Mono.just(value).map {
            it
        }.subscribe({
            settableFuture.set(it)
        }, {
            println("error")
        }, {
            completeFuture.set(complete)
        })

        Assertions.assertThat(settableFuture.get()).isEqualTo(value)
        Assertions.assertThat(completeFuture.get()).isEqualTo(complete)
    }

    @Test
    internal fun subscribeCompleteErrorTest() {
        val value = "Hello"
        val complete = "complete"
        val error = "error"
        val settableFuture = SettableListenableFuture<String>();
        val errorFuture = SettableListenableFuture<String>();
        val completeFuture = SettableListenableFuture<String>();

        Mono.just(value).map {
            throw RuntimeException()
            it
        }.subscribe({
            println("asdasd- it")
            settableFuture.set(it)
        }, {
            errorFuture.set(error)
        }, {
            println("asdasd")
            completeFuture.set(complete)
        })

        Assertions.assertThat(errorFuture.get()).isEqualTo(error)

        Assertions.assertThat(settableFuture.isDone).isFalse
        Assertions.assertThat(completeFuture.isDone).isFalse
    }
}
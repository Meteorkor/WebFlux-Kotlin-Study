package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class MonoToTest {
    @Test
    internal fun toFutureTest() {
        val text = "test"
        val future = Mono.just(text).toFuture()
        Assertions.assertThat(future.get()).isEqualTo(text)
    }

    @Test
    internal fun toFutureDoneTest() {
        val text = "test"
        val future = Mono.just(text).toFuture()
        Assertions.assertThat(future.isDone).isTrue
        Assertions.assertThat(future.get()).isEqualTo(text)
    }

    @Test
    internal fun toFutureThreadCheckTest() {
        val text = "test"
        val future = Mono.just(text)
            .map {
                Thread.sleep(500)
                it
            }
            .toFuture()

        Assertions.assertThat(future.isDone).isTrue
        Assertions.assertThat(future.get()).isEqualTo(text)
    }

    @Test
    internal fun toFutureAsyncThreadCheckTest() {
        val text = "test"
        val future = Mono.just(text)
            .map {
                Thread.sleep(500)
                it
            }.subscribeOn(Schedulers.boundedElastic())
            .toFuture()

        Assertions.assertThat(future.isDone).isFalse
        Assertions.assertThat(future.get()).isEqualTo(text)
        Assertions.assertThat(future.isDone).isTrue
    }
}
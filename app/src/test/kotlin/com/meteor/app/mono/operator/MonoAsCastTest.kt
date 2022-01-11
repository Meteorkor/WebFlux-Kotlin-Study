package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.junit.jupiter.api.Test
import org.springframework.util.concurrent.SettableListenableFuture
import reactor.core.publisher.Mono

class MonoAsCastTest {
    @Test
    internal fun asTest() {
        val text = "hello"
        val future = SettableListenableFuture<String>()
        val s = Mono.just("")
            .doOnNext {
                future.set(it)
            }
            .`as` {
//            it.block()
                text
            }
        Assertions.assertThat(future.isDone).isFalse
        Assertions.assertThat(s).isEqualTo(text)
    }

    @Test
    internal fun asBlockTest() {
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val s = Mono.just(text)
            .doOnNext {
                future.set(it)
            }
            .`as` {
                it.block()
            }
        Assertions.assertThat(future.isDone).isTrue
        Assertions.assertThat(s).isEqualTo(text)
    }

    @Test
    internal fun andTest() {
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val block = Mono.just(text).and(Mono.fromCallable {
            future.set(text)
        })
        Assertions.assertThat(future.isDone).isFalse
        Assertions.assertThat(block).isNotEqualTo(text)
    }

    @Test
    internal fun andBlockTest() {
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val block = Mono.just(text)
            .and(Mono.fromCallable {
                future.set(text)
                text
            }).block()
        Assertions.assertThat(future.isDone).isTrue
        Assertions.assertThat(block).isNotEqualTo(text)
    }

    @Test
    internal fun castTest() {
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val switchText = "SShello"
        val block = Mono.just(text).cast(String::class.java)
            .map {
                future.set(text)
                switchText
            }
        Assertions.assertThat(future.isDone).isFalse
        Assertions.assertThat(block).isNotEqualTo(text)
    }

    @Test
    internal fun castBlockTest() {
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val switchText = "SShello"
        val block = Mono.just(text).cast(String::class.java)
            .map {
                future.set(text)
                switchText
            }
            .block()
        Assertions.assertThat(future.isDone).isTrue
        Assertions.assertThat(block).isEqualTo(switchText)
    }

    @Test
    internal fun castNotBlockTest() {
        val list = Lists.list("a", "b", "c")
        val future = SettableListenableFuture<String>()
        val text = "hello"
        val switchText = "SShello"
        val block = Mono.just(list).cast(String::class.java)
            .map {
                future.set(text)
                switchText
            }.onErrorReturn(switchText)
            .block()
        Assertions.assertThat(future.isDone).isFalse
        Assertions.assertThat(block).isEqualTo(switchText)
    }
}
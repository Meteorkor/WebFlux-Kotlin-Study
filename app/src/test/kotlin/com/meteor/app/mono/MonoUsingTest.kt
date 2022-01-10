package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.junit.jupiter.api.Test
import org.springframework.util.concurrent.SettableListenableFuture
import reactor.core.publisher.Mono

class MonoUsingTest {

    @Test
    internal fun usingTest() {
        val list = Lists.newArrayList<String>()
        list.add("init")
        Mono.using({
            list.addAll(listOf("a", "b", "c"))
            list
        }, {
            Mono.fromCallable {
                Assertions.assertThat(list).size().isEqualTo(4)
            }
        }, {
            it.clear()
        }).block()
        Assertions.assertThat(list).size().isZero
    }

    @Test
    internal fun usingWhenTest() {
        val list = Lists.newArrayList<String>()
        val future = SettableListenableFuture<String>()
        list.add("init")

        Mono.usingWhen(
            Mono.just(list).map {
                list.addAll(listOf("a", "b", "c"))
                list
            }, {
                Mono.fromCallable {
                    future.set("done")
                    Assertions.assertThat(it).size().isEqualTo(4)
                }
            }, {
                Mono.fromCallable {
                    it.clear()
                }
            }
        ).block()
        Assertions.assertThat(list).size().isZero
        Assertions.assertThat(future.isDone).isTrue
    }

    @Test
    internal fun usingWhenEmptyTest() {
        val out = "out"
        val list = Lists.newArrayList<String>()
        val future = SettableListenableFuture<String>()
        list.add("init")

        val block = Mono.usingWhen<String, ArrayList<String>>(
            Mono.empty(), {
                it.addAll(listOf("a", "b", "c"))
                Mono.fromCallable {
                    Assertions.assertThat(it).size().isEqualTo(4)
                    future.set("done")
                    ""
                }
            }, {
                it.clear()
                Mono.just(out)
            }
        ).block()

        Assertions.assertThat(block).isNull()
        Assertions.assertThat(list).size().isOne
        Assertions.assertThat(future.isDone).isFalse
    }
}
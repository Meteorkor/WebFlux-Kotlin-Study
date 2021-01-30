package com.meteor.app.flux

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

/**
 * FluxTest
 *
 * @since 2021. 01. 30.
 */
class FluxTest {
    @Test
    fun valuesToFlux() {
        val fluxList = Flux.just("a", "b").log().collectList().block()
        Assertions.assertThat(fluxList).isEqualTo(listOf("a", "b"))
    }


    @Test
    fun listToFlux() {
        val list = listOf(1, 2, 3)
        val fluxList = Flux.fromIterable(list).collectList().block()
        fluxList?.forEachIndexed { index, i ->
            org.junit.jupiter.api.Assertions.assertEquals(list[index], i)
        }
    }

    @Test
    fun arrayToFlux() {
        val array = arrayOf(1, 2, 3)
        val intArray = Flux.fromArray(array).collectList().block()?.toIntArray()
        intArray?.forEachIndexed { index, i ->
            org.junit.jupiter.api.Assertions.assertEquals(array[index], i)
        }
    }

    @Test
    fun errorToFlux() {
        Assertions.assertThatThrownBy {
            Flux.error<java.lang.NullPointerException>(NullPointerException())
                    .blockLast()
        }
    }

    @Test
    fun intervalFlux() {
        val fluxList = Flux.interval(Duration.ofMillis(100))
                .take(10)
                .collectList().block()

        Assertions.assertThat(fluxList).isEqualTo(listOf(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L))
    }

    @Test
    fun orFlux() {
        //or, 시그널이 빨리오는쪽으로 수행
        val fluxList = Flux.interval(Duration.ofMillis(300))
                .map {
                    println("it-1 $it")
                    it
                }
                .take(10)
                .or(Flux.interval(Duration.ofMillis(200)).map {
                    println("it-2 $it")
                    it
                }
                        .take(30))
                .collectList().block()
        fluxList?.forEach {
            println(it)
        }
    }

    @Test
    internal fun completeCheck() {
        Flux.just("foot", "bar")
                .let {
                    StepVerifier.create(it)
                            .expectNext("foot", "bar")
                            .expectComplete().verify()
                }
    }

    @Test
    internal fun failCheck() {
        Flux.just("foot", "bar")
                .map {
                    RuntimeException()
                }
                .let {
                    StepVerifier.create(it)
                            .expectError().verify()
                }
    }

    //
//    @Test
//    internal fun assertCheck() {
//        Flux.just("foot", "bar")
//                .let {
//                    StepVerifier.create(it)
////                            .assertNext {
////                                when (it) {
////                                    "foot", "bar" -> it
////                                    else -> AssertionError(it)
////                                }
////                            }
//                            .expectNext("foot", "bar")
//                            .expectComplete()
//                            .verify()
//                }
//        Flux.just("foot", "bar")
//                .let {
//                    StepVerifier.create(it)
//                            .assertNext {
//                                when (it) {
//                                    "foot!!", "bar1212" -> it
//                                    else -> AssertionError(it)
//                                }
//                            }.expectError()
//                            .verify()
//                }
//
//    }

}
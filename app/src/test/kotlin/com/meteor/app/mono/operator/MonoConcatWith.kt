package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoConcatWith {
    @Test
    internal fun concatWithTest() {
        val a = "a"
        val b = "b"
        val collectList = Mono.just(a).concatWith(Mono.just(b)).collectList()
        StepVerifier.create(collectList)
            .expectNextMatches {
                it.size == 2
            }
            .expectComplete()
            .verify()
    }
}
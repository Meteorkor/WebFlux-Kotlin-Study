package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoTransformTest {
    @Test
    internal fun transformTest() {
        val text = "test"
        val text2 = "test2"
        val transformMono = Mono.just(text).transform {
            //pub->pub
            it.map { text2 }
        }
        StepVerifier.create(transformMono)
            .expectNext(text2)
            .verifyComplete()
    }

    @Test
    internal fun transformDeferredTest() {
        val text = "test"
        val text2 = "test2"
        val transformMono = Mono.just(text).transformDeferred {
            //pub->pub
            it.map { text2 }
        }
        StepVerifier.create(transformMono)
            .expectNext(text2)
            .verifyComplete()

    }
}
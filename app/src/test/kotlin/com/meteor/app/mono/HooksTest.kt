package com.meteor.app.mono

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Operators
import reactor.test.StepVerifier

class HooksTest {

    @BeforeEach
    internal fun setUp() {
        Hooks.onEachOperator("keyOp", Operators.lift { t, u ->
            DecorateSubscriber(u)
        })
    }

    @Test
    internal fun decorateSubsciberTest() {
        StepVerifier.create(Flux.just("1", "2", "3"))
            .expectNext("1", "1", "2", "2", "3", "3")
            .verifyComplete()
    }

    class DecorateSubscriber<T>(val coreSubscriber: CoreSubscriber<T>) : CoreSubscriber<T> {
        override fun onSubscribe(s: Subscription) {
            coreSubscriber.onSubscribe(s)
        }

        override fun onNext(t: T) {
            coreSubscriber.onNext(t)
            coreSubscriber.onNext(t)
        }

        override fun onError(t: Throwable?) {
            coreSubscriber.onError(t)
        }

        override fun onComplete() {
            coreSubscriber.onComplete()
        }
    }

}
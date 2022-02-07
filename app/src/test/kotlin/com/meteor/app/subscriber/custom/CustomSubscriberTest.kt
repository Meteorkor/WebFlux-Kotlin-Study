package com.meteor.app.subscriber.custom

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.util.concurrent.SettableListenableFuture
import reactor.core.publisher.Mono
import java.util.concurrent.ExecutionException
import java.util.concurrent.atomic.AtomicInteger

/**
 * 현재, Mono만 대응할 수 있는 형태
 */
class CustomSubscriberTest {
    @Test
    internal fun callDebuggingSubscriber() {
        val hello = "hello"
        val stepAtomic = AtomicInteger()

        val subscriber = object : Subscriber<String> {
            private lateinit var subscription: Subscription
            private val settableListenableFuture = SettableListenableFuture<String>()
            fun isDone() = settableListenableFuture.isDone()
            fun get() = settableListenableFuture.get()

            override fun onSubscribe(s: Subscription?) {
                Assertions.assertEquals(0, stepAtomic.getAndIncrement())
                subscription = s!!
                subscription.request(1)
            }

            override fun onNext(t: String?) {
                Assertions.assertEquals(1, stepAtomic.getAndIncrement())
                settableListenableFuture.set(t)
            }

            override fun onError(t: Throwable?) {
                subscription.cancel()
            }

            override fun onComplete() {
                Assertions.assertEquals(2, stepAtomic.getAndIncrement())
                subscription.cancel()
            }

        }

        Mono.just(hello).subscribeWith(subscriber)
        subscriber.get()
    }

    @Test
    internal fun futureSubscriberOkTest() {
        val hello = "hello"
        val subscriber = FutureSubscriber<String>()

        Mono.just(hello).subscribeWith(subscriber)
        Assertions.assertEquals(hello, subscriber.get())
    }

    @Test
    internal fun futureSubscriberErrorTest() {
        val subscriber = FutureSubscriber<String>()
        Mono.error<String>(NullPointerException()).subscribeWith(subscriber)
        Assertions.assertThrows(ExecutionException::class.java) {
            subscriber.get()
        }
    }
}
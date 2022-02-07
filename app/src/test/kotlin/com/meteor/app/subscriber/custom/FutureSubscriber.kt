package com.meteor.app.subscriber.custom

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.util.concurrent.SettableListenableFuture

class FutureSubscriber<T> : Subscriber<T> {
    private lateinit var subscription: Subscription
    private val settableListenableFuture = SettableListenableFuture<T>()
    private var value: T? = null

    fun isDone() = settableListenableFuture.isDone()
    fun get() = settableListenableFuture.get()

    override fun onSubscribe(s: Subscription?) {
        subscription = s!!
        subscription.request(1)
    }

    override fun onNext(t: T) {
        value = t
    }

    override fun onError(t: Throwable) {
        settableListenableFuture.setException(t)
        subscription.cancel()
    }

    override fun onComplete() {
        subscription.cancel()
        settableListenableFuture.set(value)
    }
}
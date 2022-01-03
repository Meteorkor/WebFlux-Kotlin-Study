package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

class MonoFromTest {
    @Test
    internal fun fromCallableTest() {
        val text = "Hello"
        val callable = {
            text
        }
        val fromCallable = Mono.fromCallable(callable)

        Assertions.assertThat(fromCallable.block()).isEqualTo(text)
    }

//    @Test
//    internal fun fromCompletionStageTest() {
//        val text = "Hello"
//
//        val minimalCompletionStage = CompletableFuture.runAsync {
//            text
//        }.minimalCompletionStage()
//
//        val fromCompletionStage = Mono.fromCompletionStage(minimalCompletionStage)
//        Assertions.assertThat(fromCompletionStage.block()).isEqualTo(text)
//    }

    //fromCompletionStage

    //fromDirect

    //fromFuture

    //fromRunnable

    //fromSupplier
}
package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration

class MonoOnContinueTest {

    @Test
    internal fun exceptionErrorContinueRetry() {

        val map = Flux.range(0, 100)
            .map {
                println("it: ${it}")
                if (it % 10 == 0) {
                    println("${System.currentTimeMillis()}]it-error: ${it}")
                    throw RuntimeException();
                }
                it
            }
            .onErrorContinue { th, obj ->
                th.printStackTrace()
            }.delayElements(Duration.ofMillis(1000))
            .doOnNext{
                println("tail: ${it}")
            }

        map.blockLast()
        //기대와는 달리 모든 item이 delay가 적용된다.


    }

}
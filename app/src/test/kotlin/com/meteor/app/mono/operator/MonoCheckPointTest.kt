package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger

class MonoCheckPointTest {

    @Test
    internal fun checkPointTest() {
        val atomicInteger = AtomicInteger()
        val fromCallableMono = Mono.fromCallable {
            atomicInteger.incrementAndGet()
        }.checkpoint("desc")
            .flatMap {
//                Mono.error<String>(RuntimeException("asd"))
                Mono.just("asd")
            }.map { "asd" }
            .checkpoint("desc2")
            .map { "asd222" }
            .flatMap {
                Mono.error<String>(RuntimeException("asd"))
            }
            .checkpoint()//에러 트래킹 목적

        StepVerifier.create(fromCallableMono)
            .expectError()
    }
}
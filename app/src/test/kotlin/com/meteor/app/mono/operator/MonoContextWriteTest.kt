package com.meteor.app.mono.operator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoContextWriteTest {
    @Test
    internal fun contextWriteTest() {
        val keyName = "value"
        val value = "Hello"
        val deferContextualMono = Mono.deferContextual {
            val get = it.get<Any>(keyName)
            Mono.just(get)
        }

        val contextMono = deferContextualMono.contextWrite {
            it.put(keyName, value)
        }
        StepVerifier.create(contextMono)
            .expectNext(value)
            .verifyComplete()

        val newValue = "12312312"
        val blockRewriteMono = contextMono.contextWrite {
            val put = it.put(keyName, newValue)
            put
        }

        StepVerifier.create(blockRewriteMono)
            .assertNext {
                Assertions.assertThat(it).isNotEqualTo(newValue)
                Assertions.assertThat(it).isEqualTo(value)
            }.verifyComplete()
    }
}
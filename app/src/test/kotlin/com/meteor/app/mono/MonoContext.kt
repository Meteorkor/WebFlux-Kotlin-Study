package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class MonoContext {

    @Test
    internal fun contextNotSetTest() {
        val keyName = "value"
        val deferContextualMono = Mono.deferContextual {
            val get = it.get<Any>(keyName)
            Mono.just(get)
        }
        Assertions.assertThatThrownBy {
            val block1 = deferContextualMono.block()
        }//NoSuchElementException("Context is empty")
    }

    @Test
    internal fun deferContextualWriteTest() {
        val keyName = "value"
        val value = "Hello"
        val deferContextualMono = Mono.deferContextual {
            val get = it.get<Any>(keyName)
            Mono.just(get)
        }
        val block = deferContextualMono.contextWrite {
            it.put(keyName, value)
        }.block()
        Assertions.assertThat(block).isEqualTo(value)
    }

    @Test
    internal fun deferContextualTest() {
        val keyName = "value"
        val value = "Hello"
        val deferContextualMono = Mono.deferContextual {
            val get = it.get<Any>(keyName)
            Mono.just(get)
        }

        val contextMono = deferContextualMono.contextWrite {
            it.put(keyName, value)
        }
        val block1 = contextMono.block()
        Assertions.assertThat(block1).isEqualTo(value)

        val newValue = "12312312"
        val blockRewriteMono = contextMono.contextWrite {
            val put = it.put(keyName, newValue)
            put
        }
        Assertions.assertThat(blockRewriteMono.block()).isNotEqualTo(newValue)
        Assertions.assertThat(blockRewriteMono.block()).isEqualTo(value)
    }


    //todo contextMono.transformDeferredContextual
}
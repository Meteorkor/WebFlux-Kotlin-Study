package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MonoExpandTest {
    @Test
    internal fun expandTest() {
        val text = "hello"
        val expandMono = Mono.just(text).expand {
            Mono.just(text)
        }//{"hello", "hello", ...}
        //infinity pub
    }

    @Test
    internal fun expandFluxTest() {
        val num = 3
        val expandFlux = Mono.just(num).expand {
            Flux.range(0, num)
        }//{0,1,2}
    }

    @Test
    internal fun expandDeepTest() {
        val text = "hello"
        val num = 3
        val expandFlux = Mono.just(num).expandDeep {
            Flux.range(0, num)
        }//{0,0,0,0,0,0,0,0,0,..............}
    }
}
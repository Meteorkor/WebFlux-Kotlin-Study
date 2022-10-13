package com.meteor.app.mono.operator

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger

class MonoOnErrorResumeTest {
    @Test
    internal fun onErrorReturnTest() {
        val message = "testMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw RuntimeException("testException")
            message
        }.map {
            it + it//logic
        }.onErrorReturn(message)

        StepVerifier.create(onErrorResumeMono)
            .expectNext(message)
            .verifyComplete()
    }

    @Test
    internal fun onErrorReturnCatchTest() {
        val message = "testMsg"
        val npeMsg = "npeMsg"
        val illegalMsg = "illegalMsg"
        val runtimeMsg = "RUNTIME?"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorReturn(IllegalArgumentException::class.java, illegalMsg)
            .onErrorReturn(RuntimeException::class.java, runtimeMsg)

        StepVerifier.create(onErrorResumeMono)
            .expectNext(illegalMsg)
            .verifyComplete()
    }

    @Test
    internal fun onErrorReturnCatchCheckOrderTest() {
        val message = "testMsg"
        val npeMsg = "npeMsg"
        val illegalMsg = "illegalMsg"
        val runtimeMsg = "RUNTIME?"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorReturn(RuntimeException::class.java, runtimeMsg)
            .onErrorReturn(IllegalArgumentException::class.java, illegalMsg)

        StepVerifier.create(onErrorResumeMono)
            .expectNext(runtimeMsg)
            .verifyComplete()
    }

    @Test
    internal fun onErrorResumeIgnoreJustTest() {
        val message = "testMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw RuntimeException("testException")
            message
        }.map {
            it + it//logic
        }.onErrorResume {
            Mono.just(message)//onErrorSame
        }

        StepVerifier.create(onErrorResumeMono)
            .expectNext(message)
            .verifyComplete()
    }

    @Test
    internal fun onErrorResumeErrorProcessingTest() {
        val message = "testMsg"
        val errorMessage = "testException"
        val onErrorResumeMono = Mono.just(message).map {
            throw RuntimeException(errorMessage)
            message
        }.onErrorResume {
            Mono.just(it.message!!)
        }

        StepVerifier.create(onErrorResumeMono)
            .expectNext(errorMessage)
            .verifyComplete()
    }

    @Test
    internal fun onErrorResumeCatchTest() {
        val message = "testMsg"
        val npeMsg = "npeMsg"
        val illegalMsg = "illegalMsg"
        val runtimeMsg = "RUNTIME?"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorResume(IllegalArgumentException::class.java) {
            Mono.just(it.message!!)
        }.onErrorResume(RuntimeException::class.java) {
            Mono.just(runtimeMsg)
        }

        StepVerifier.create(onErrorResumeMono)
            .expectNext(illegalMsg)
            .verifyComplete()
    }

    @Test
    internal fun onErrorResumeCatchCheckOrderTest() {
        val message = "testMsg"
        val npeMsg = "npeMsg"
        val illegalMsg = "illegalMsg"
        val runtimeMsg = "RUNTIME?"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorResume(RuntimeException::class.java) {
            Mono.just(runtimeMsg)
        }.onErrorResume(IllegalArgumentException::class.java) {
            Mono.just(it.message!!)
        }
        StepVerifier.create(onErrorResumeMono)
            .expectNext(runtimeMsg)
            .verifyComplete()
    }

    @Test
    internal fun onErrorResumeProcessAndThrowTest() {
        val atomicInteger = AtomicInteger()
        val message = "testMsg"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorResume(RuntimeException::class.java) { ex ->
            Mono.defer {
                Mono.just(atomicInteger.incrementAndGet())
            }.flatMap {
                Mono.error(ex)
            }
        }
        StepVerifier.create(onErrorResumeMono)
            .verifyError()

        Assertions.assertEquals(1, atomicInteger.get())
    }

    @Test
    internal fun onErrorStopNonContinueTest() {
        val message = "testMsg"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorStop()
            .map {
                println("!!!@")
                //not called
                it
            }

        StepVerifier.create(onErrorResumeMono)
            .verifyError()
    }

    @Test
    internal fun exceptionOnErrorResumeExceptionTest() {
        val message = "testMsg"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorResume {
            Mono.just("retry")
        }.map {
            throw IllegalArgumentException(illegalMsg)
        }

        StepVerifier.create(onErrorResumeMono)
            .verifyError()
    }

    @Test
    internal fun exceptionOnErrorStopOnErrorResumeTest() {
        val message = "testMsg"
        val retry = "retry"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorResume {
            Mono.just(retry)
        }

        StepVerifier.create(onErrorResumeMono)
            .expectNext(retry)
            .verifyComplete()
    }

    @Test
    internal fun testZip() {

        val mono1 = Mono.just("test")
        val mono2 = Mono.empty<String>()


        val flatMap = mono1.zipWith(mono2).flatMap<Any> {
            println("it.t1 : ${it.t1} it.t2 : ${it.t2}")
            Mono.just(it.t1)
        }.block()

        println("flatMap : ${flatMap}")

    }


    @Test
    internal fun exceptionOnErrorStopOnErrorContinueTest() {
        val atomicInteger = AtomicInteger()
        val message = "testMsg"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }.onErrorStop()
            .map {
                atomicInteger.incrementAndGet()
            }

        StepVerifier.create(onErrorResumeMono)
            .verifyError()

        Assertions.assertEquals(0, atomicInteger.get())
    }

    @Test
    internal fun onErrorStopVsNormalTest() {
        val message = "testMsg"
        val illegalMsg = "illegalMsg"
        val onErrorResumeMono = Mono.just(message).map {
            throw IllegalArgumentException(illegalMsg)
//            throw NullPointerException(illegalMsg)
            message
        }

        StepVerifier.create(onErrorResumeMono)
            .verifyError()
    }

}
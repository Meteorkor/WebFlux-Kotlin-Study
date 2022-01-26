package com.meteor.app.mono.operator

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

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

//    @Test
//    internal fun onErrorStopTest() {
//        val message = "testMsg"
//        val illegalMsg = "illegalMsg"
//        val onErrorResumeMono = Mono.just(message).map {
//            throw IllegalArgumentException(illegalMsg)
////            throw NullPointerException(illegalMsg)
//            message + "map"
//        }.onErrorContinue { t, obj ->
//// obj == ${message}
//
//        }.map {
//            it
//        }
//            .onErrorStop()
//            .map {
//                //not called
//                it
//            }
//
//        StepVerifier.create(onErrorResumeMono)
//            .verifyError()
//    }

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
                //not called
                it
            }

        StepVerifier.create(onErrorResumeMono)
            .verifyError()
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
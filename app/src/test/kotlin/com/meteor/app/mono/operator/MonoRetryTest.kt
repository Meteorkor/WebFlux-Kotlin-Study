package com.meteor.app.mono.operator

import com.meteor.app.test.TestTimeUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class MonoRetryTest {

    @Test
    internal fun retryTest() {
        val atomicInteger = AtomicInteger()
        val num = 3
        val retryMono = Mono.just(num)
            .map {
                atomicInteger.incrementAndGet()
                throw RuntimeException()
                it
            }
            .retry()//infinity try

        StepVerifier.create(retryMono)
            .verifyTimeout(Duration.ofMillis(1000))

        Assertions.assertThat(atomicInteger.get()).isGreaterThan(100)
    }

    @Test
    internal fun retryCntTest() {
        val retryCnt = 5L
        val atomicInteger = AtomicInteger()
        val num = 3
        val retryMono = Mono.just(num)
            .map {
                atomicInteger.incrementAndGet()
                throw RuntimeException()
                it
            }
            .retry(retryCnt)//retryCnt

        StepVerifier.create(retryMono)
            .verifyError()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(retryCnt.toInt() + 1)
    }

    @Test
    internal fun retryWhenDelayTest() {
        val retryCnt = 2L
        val atomicInteger = AtomicInteger()
        val num = 3
        val delayTime = 1000L

        val time = TestTimeUtil.time {
            val retryMono = Mono.just(num)
                .map {
                    atomicInteger.incrementAndGet()
                    throw RuntimeException()
                    it
                }.retryWhen(Retry.fixedDelay(retryCnt, Duration.ofMillis(delayTime)))
            StepVerifier.create(retryMono)
                .verifyError()
        }
        Assertions.assertThat(atomicInteger.get()).isEqualTo(retryCnt.toInt() + 1)

        Assertions.assertThat(time).isGreaterThan(delayTime * retryCnt)
            .isLessThan(delayTime * (retryCnt + 1))
    }

    @Test
    internal fun retryWhenMaxTest() {
        val retryCnt = 2L
        val atomicInteger = AtomicInteger()
        val num = 3
        val delayTime = 1000L

        val retryMono = Mono.just(num)
            .map {
                atomicInteger.incrementAndGet()
                throw RuntimeException()
                it
            }.retryWhen(Retry.max(retryCnt))

        StepVerifier.create(retryMono)
            .verifyError()

        Assertions.assertThat(atomicInteger.get()).isEqualTo(retryCnt.toInt() + 1)
    }

    @Test
    internal fun retryWhenMaxExceptionFilterTest() {
        val retryCnt = 2L
        val atomicInteger = AtomicInteger()
        val num = 3

        val retryMono = Mono.just(num)
            .map {
                atomicInteger.incrementAndGet()
                throw RuntimeException()
                it
            }.retryWhen(Retry.max(retryCnt).filter {
                it is RuntimeException
            })

        StepVerifier.create(retryMono)
            .verifyError()
        Assertions.assertThat(atomicInteger.get()).isEqualTo(retryCnt.toInt() + 1)
    }


    @Test
    internal fun retryWhenMaxExceptionFilterNotTest() {
        val retryCnt = 2L
        val atomicInteger = AtomicInteger()
        val num = 3

        val retryMono = Mono.just(num)
            .map {
                atomicInteger.incrementAndGet()
                throw Exception()
                it
            }.retryWhen(Retry.max(retryCnt).filter {
                it is RuntimeException
            })

        StepVerifier.create(retryMono)
            .verifyError()
        Assertions.assertThat(atomicInteger.get()).isEqualTo(1)
    }
    //TODO .doAfterRetry(


    @DisplayName("에러 발생시 특정 로직을 처리하고 다시 retry")
    @Test
    internal fun retryProcess() {
        val num = 3
        val retryCnt = 5L
        val retryCntCheck = AtomicInteger()
        val onErrorResumeCnt = AtomicInteger()
        val retryMono = Mono.just(num)
            .map {
                retryCntCheck.incrementAndGet()
                throw RuntimeException()
                it
            }.onErrorResume { th ->
                Mono.defer {
                    Mono.just(onErrorResumeCnt.incrementAndGet())
                }.flatMap { Mono.error(th) }
            }
            .retry(retryCnt)//retryCnt
//            .onErrorResume()

        StepVerifier.create(retryMono)
            .verifyError()

        Assertions.assertThat(retryCntCheck.get()).isEqualTo(retryCnt.toInt() + 1)
        Assertions.assertThat(onErrorResumeCnt.get()).isEqualTo(retryCnt.toInt() + 1)
    }

}
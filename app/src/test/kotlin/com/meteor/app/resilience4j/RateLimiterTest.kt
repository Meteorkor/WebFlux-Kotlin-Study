package com.meteor.app.resilience4j

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import java.time.Duration
import java.util.stream.IntStream

class RateLimiterTest {

    @Test
    internal fun rateLimiterTest() {
        val tpsPerServer = 1
        val timeDuration = 1L

        val rateLimiter = RateLimiter.of(
            "tempLimiter", RateLimiterConfig.custom()
                .limitRefreshPeriod(
                    Duration.ofSeconds(1)
                ).limitForPeriod(tpsPerServer)
                .build()
        )


        val permits = 1
        rateLimiter.acquirePermission(permits)
        IntStream.range(0, 10)
            .forEach {
                val sw = StopWatch()
                sw.start()
                //1tps로 제한되어서 확보될때까지 대기
                rateLimiter.acquirePermission(permits)
                sw.stop()

//                println("sw.totalTimeSeconds : ${sw.totalTimeSeconds}")

                Assertions.assertTrue(sw.totalTimeSeconds >= permits * 0.9)
                Assertions.assertTrue(sw.totalTimeSeconds <= (permits + 1))
            }
    }


    @Test
    internal fun rateLimiterMillisTest() {
        val tpsPerServer = 1
        val timeDuration = 1L

        val rateLimiter = RateLimiter.of(
            "tempLimiter", RateLimiterConfig.custom()
                .limitRefreshPeriod(
                    Duration.ofMillis(100)
                ).limitForPeriod(tpsPerServer)
                .build()
        )


        val permits = 1
        rateLimiter.acquirePermission(permits)
        IntStream.range(0, 10)
            .forEach {
                val sw = StopWatch()
                sw.start()
                //10tps로 제한되어서 확보될때까지 대기(100ms)
                rateLimiter.acquirePermission(permits)
                sw.stop()

                println("sw.totalTimeMillis : ${sw.totalTimeMillis}")


                //mill 는 더 부정확해짐
                Assertions.assertTrue(sw.totalTimeMillis >= (permits * 100) * 0.9)
                Assertions.assertTrue(sw.totalTimeMillis <= ((permits * 100) + 1))
            }
    }

}
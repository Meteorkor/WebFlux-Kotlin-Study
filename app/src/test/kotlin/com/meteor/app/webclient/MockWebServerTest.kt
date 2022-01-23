package com.meteor.app.webclient

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class MockWebServerTest {

    companion object {
        lateinit var mockBackEnd: MockWebServer
        lateinit var webClientTest: WebClient

        @BeforeAll
        @JvmStatic
        fun setUp() {
            mockBackEnd = MockWebServer()
            mockBackEnd.start()
            webClientTest =
                WebClient.builder().baseUrl("http://${mockBackEnd.hostName}:${mockBackEnd.port}").build()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mockBackEnd.shutdown();
        }
    }

    /**
     * https://github.com/square/okhttp/issues/6281
     * not set body, notDelayResponse
     */
    @Test
    internal fun issue() {
        mockBackEnd.enqueue(
            MockResponse()
                .setBodyDelay(5, TimeUnit.SECONDS)
                .setResponseCode(200)
        )

//is not bug
//        mockBackEnd.enqueue(
//            MockResponse()
//                .setBodyDelay(5, TimeUnit.SECONDS)
//                .setBody("Anything") //body cannot be null otherwise delay doesn't work
//                .setResponseCode(200)
//        )

        val default = "default"
        val stopWatch = StopWatch()
        stopWatch.start()
        val exchangeToMono = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }.timed()
        StepVerifier.create(exchangeToMono)
            .verifyComplete()
        stopWatch.stop()
        Assertions.assertThat(stopWatch.totalTimeMillis).isLessThan(1000)
    }
}
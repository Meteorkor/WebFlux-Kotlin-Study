package com.meteor.app.webclient

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.concurrent.TimeUnit


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WebClientMockWebServerTest {
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

    @Test
    internal fun getTest1() {
        val body = "{\"error_code\": null, \"error_message\": null}"
        mockBackEnd.enqueue(
            MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        )

        val exchangeToMono = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }
        StepVerifier.create(exchangeToMono)
            .expectNext(body)
            .verifyComplete()
    }


    @Test
    internal fun asyncCallTest() {
        val body = "{\"error_code\": null, \"error_message\": null}"
        mockBackEnd.enqueue(
            MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body).setBodyDelay(500, TimeUnit.MILLISECONDS)
        )
        mockBackEnd.enqueue(
            MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body).setBodyDelay(500, TimeUnit.MILLISECONDS)
        )

        val callMono1 = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }
        val callMono2 = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }

        val asyncMono = Mono.zip(callMono1, callMono2).timed()

        StepVerifier.create(asyncMono)
            .expectNextMatches {
                val get = it.get()
                it.elapsed().toMillis() > 500 && get.t1 == body && get.t2 == body
            }
            .verifyComplete()
    }

    @Test
    @Order(Int.MAX_VALUE)
    internal fun getTestNoEnqueue() {
        val exchangeToMono = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }

        StepVerifier.create(exchangeToMono)
            .verifyTimeout(Duration.ofSeconds(1))
    }
}
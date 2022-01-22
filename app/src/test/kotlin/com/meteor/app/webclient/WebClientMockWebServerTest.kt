package com.meteor.app.webclient

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.time.Duration


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
        webClientTest =
            WebClient.builder().baseUrl("http://${mockBackEnd.hostName}:${mockBackEnd.port}").build()

        val exchangeToMono = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }
        StepVerifier.create(exchangeToMono)
            .expectNext(body)
            .verifyComplete()
    }

    @Test
    internal fun getTestNoEnqueue() {
        val exchangeToMono = webClientTest.get().uri("/api/report").exchangeToMono {
            it.bodyToMono(String::class.java)
        }

        StepVerifier.create(exchangeToMono)
            .verifyTimeout(Duration.ofSeconds(1))
    }
}
package com.meteor.app.mono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import javax.management.InvalidAttributeValueException

class MonoCreateJustDefTest {
    @Test
    internal fun createSuccessMono() {
        val create = Mono.create<String> {
            Thread.sleep(500)
            it.success()
        }
        val block = create.block()
        Assertions.assertThat(block).isNull()

        val retStr = "retStr"
        val create2 = Mono.create<String> {
            Thread.sleep(500)
            it.success(retStr)
        }
        val block2 = create2.block()
        Assertions.assertThat(block2).isNotNull()
        Assertions.assertThat(block2).isEqualTo(retStr)
    }

    @Test
    internal fun createErrorMono() {
        val create = Mono.create<String> {
            Thread.sleep(500)
            it.error(InvalidAttributeValueException())
        }

        Assertions.assertThatThrownBy {//will throw, InvalidAttributeValueException()
            val block = create.block()
        }
    }

    @Test
    internal fun justMono() {
        val justValue = "hello"
        val just = Mono.just(justValue)
        Assertions.assertThat(just.block()).isEqualTo(justValue)
    }

    @Test
    internal fun defTest() {
        val str = "Hello"
        val createdMono = Mono.just(str)

        val block = Mono.defer { createdMono }.block()
        Assertions.assertThat(block).isEqualTo(str)
    }


    @Test
    fun justCreateCompareTest() {
        Assertions.assertThatThrownBy {
            Mono.just(throwReturn()).block()
        }

        Assertions.assertThatThrownBy {
            Mono.create<String> {
                it.success(throwReturn())
            }.block()
        }

        Assertions.assertThatThrownBy {
            Mono.just(throwReturn())
        }

        Mono.create<String> {
            it.success(throwReturn())
        }
    }

    private fun throwReturn(): String {
        throw InvalidAttributeValueException()
        return "Hello!!"
    }
}
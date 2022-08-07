package com.meteor.app.file

import org.assertj.core.util.Lists
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Flux
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.stream.Stream

class FilePubSubTest {

    @Test
    internal fun fileLinesStreamClosedTest() {
        val file = File.createTempFile("pre", "suf")
        file.deleteOnExit()

        val fileContentSample = Lists.list("a", "b", "c")
        Files.write(file.toPath(), fileContentSample, StandardOpenOption.CREATE)

        val iterator: Iterator<String> = fileContentSample.iterator()

        val linesStream = Files.lines(file.toPath())



        linesStream
            .forEach {
                Assertions.assertEquals(
                    iterator.next(),
                    it
                )
            }

        Assertions.assertFalse(fileStreamClosedCheck(linesStream))
    }


    @Test
    internal fun fileSimpleLinePublisherRead() {
        val file = File.createTempFile("pre", "suf")
        file.deleteOnExit()

        val fileContentSample = Lists.list("a", "b", "c")
        Files.write(file.toPath(), fileContentSample, StandardOpenOption.CREATE)

        val iterator: Iterator<String> = fileContentSample.iterator()

        val fileStream = Files.lines(file.toPath())
        val fileSimpleLinePublisher = Flux.fromStream(fileStream)
            .doOnNext {
                Assertions.assertEquals(
                    iterator.next(),
                    it
                )
            }
        fileSimpleLinePublisher.blockLast()

        /**
         * 예상 했던것은 false지만, closeAction이 호출되어있음
         * Assertions.assertFalse(fileStreamClosedCheck(fileStream))
         */
        Assertions.assertTrue(fileStreamClosedCheck(fileStream))
    }


    @Test
    internal fun fileSimpleLinePublisherReadWellClosed() {
        val file = File.createTempFile("pre", "suf")
        file.deleteOnExit()

        val fileContentSample = Lists.list("a", "b", "c")
        Files.write(file.toPath(), fileContentSample, StandardOpenOption.CREATE)

        val iterator: Iterator<String> = fileContentSample.iterator()

        val fileStream = Files.lines(file.toPath())

        val fileSimpleLinePublisher = Flux.using(
            { fileStream },
            { Flux.fromStream(it) },
            Stream<String>::close
        ).doOnNext {
            Assertions.assertEquals(
                iterator.next(),
                it
            )
        }

        fileSimpleLinePublisher.blockLast()

        Assertions.assertTrue(fileStreamClosedCheck(fileStream))
    }

    private fun <T> fileStreamClosedCheck(fileStream: Stream<T>): Boolean {
        val sourceCloseAction = ReflectionTestUtils.getField(fileStream, "sourceCloseAction")

        sourceCloseAction?.let {
            val fileChannel = ReflectionTestUtils.getField(sourceCloseAction, "arg$1")!!//FileChannel
            val fileChannelClosed = ReflectionTestUtils.getField(fileChannel, "closed") as Boolean
            return fileChannelClosed;
        }
        return true;
    }

}
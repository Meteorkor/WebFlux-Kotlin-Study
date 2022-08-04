package com.meteor.app.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors
import java.util.stream.IntStream

class SortTest {

    @Test
    internal fun sortTest() {

        val collect = IntStream.range(0, 10000).mapToObj {
            Pair(ThreadLocalRandom.current().nextInt(0, 1000), ThreadLocalRandom.current().nextInt(0, 1000))
        }.collect(Collectors.toList())

        val collectCopy1 = ArrayList(collect)
        val collectCopy2 = ArrayList(collect)

        run {
            collectCopy1.sortWith(Comparator.comparing<Pair<Int, Int>?, Int?> { it.first }.thenBy { it.second })
            Collections.sort(collectCopy2, Comparator.comparing { it.first })
            Collections.sort(collectCopy2, Comparator.comparing { it.second })

            Assertions.assertFalse(collectCopy1.equals(collectCopy2))
        }

        run {
            collectCopy1.sortWith(Comparator.comparing<Pair<Int, Int>?, Int?> { it.first }.thenBy { it.second })
            collectCopy2.sortWith(Comparator.comparing<Pair<Int, Int>?, Int?> { it.first }.thenBy { it.second })
            Assertions.assertTrue(collectCopy1.equals(collectCopy2))
        }
    }

}
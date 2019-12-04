package dev.serch.adventofcode

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import dev.serch.adventofcode.ProcessIntCode.Companion.replace

class IntCodeSpec : StringSpec({
    val processIntCode = ProcessIntCode()

    "test samples should return expected result" {
        forall(
                row(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50), listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)),
                row(listOf(1, 0, 0, 0, 99), listOf(2, 0, 0, 0, 99)),
                row(listOf(2, 3, 0, 3, 99), listOf(2, 3, 0, 6, 99)),
                row(listOf(2, 4, 4, 5, 99, 0), listOf(2, 4, 4, 5, 99, 9801)),
                row(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99), listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)),
                row(listOf(1, 0, 0, 3, 2, 0, 3, 1, 30), listOf(1, 2, 0, 2, 2, 0, 3, 1, 30)),
                row(listOf(1, 0, 0, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 99), listOf(1, 0, 0, 2, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 99))
        ) { mass, expectedProcessed ->
            processIntCode(mass) shouldBe expectedProcessed
        }
    }

    "test with input should solve the first puzzle" {
        val input = javaClass.classLoader.getResource("IntCodeInput.txt")
                .readText()
                .split(",")
                .map { it.toInt() }
        val input1202 = input.replace(1, 12).replace(2, 2)
        processIntCode(input1202) shouldBe listOf(4945026, 12, 2, 2, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 10, 1, 48, 1, 19, 5, 49, 1, 23, 9, 52, 2, 27, 6, 104, 1, 31, 6, 106, 2, 35, 9, 318, 1, 6, 39, 320, 2, 10, 43, 1280, 1, 47, 9, 1283, 1, 51, 6, 1285, 1, 55, 6, 1287, 2, 59, 10, 5148, 1, 6, 63, 5150, 2, 6, 67, 10300, 1, 71, 5, 10301, 2, 13, 75, 51505, 1, 10, 79, 51509, 1, 5, 83, 51510, 2, 87, 10, 206040, 1, 5, 91, 206041, 2, 95, 6, 412082, 1, 99, 6, 412084, 2, 103, 6, 824168, 2, 107, 9, 2472504, 1, 111, 5, 2472505, 1, 115, 6, 2472507, 2, 6, 119, 4945014, 1, 5, 123, 4945015, 1, 127, 13, 4945020, 1, 2, 131, 4945022, 1, 135, 10, 0, 99, 2, 14, 0, 0)
    }

    "test day2.2" {
        val input = javaClass.classLoader.getResource("IntCodeInput.txt")
                .readText()
                .split(",")
                .map { it.toInt() }
        val findNounAndVerb = FindNounAndVerb()
        findNounAndVerb(input, 19690720) shouldBe 5296
    }
})

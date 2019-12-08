package dev.serch.adventofcode

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class DecodeImageSpec : StringSpec({
    val splitLayers = SplitLayers()
    val checkImageConsistency = CheckImageConsistency()
    val combineLayers = CombineLayers()
    val printImage = PrintImage()

    "test day8 samples" {
        splitLayers("123456789012", 3, 2) shouldBe listOf(
                listOf(1, 2, 3, 4, 5, 6),
                listOf(7, 8, 9, 0, 1, 2)
        )
    }

    "test day8 consistency samples" {
        checkImageConsistency("123456789012", 3, 2) shouldBe 1
    }

    "test day8.1 with input should get the result" {
        val input = javaClass.classLoader.getResource("BIOSPasswordImageInput.txt").readText()
        checkImageConsistency(input, 25, 6) shouldBe 2080
    }

    "test day8.2 with input should get the result" {
        val input = javaClass.classLoader.getResource("BIOSPasswordImageInput.txt").readText()
        printImage(combineLayers(input, 25, 6)) shouldBe
                "\n■□□■■□■■□■□□□■■■□□■■□■■■□\n" +
                "□■■□■□■■□■□■■□■□■■□■□■■■□\n" +
                "□■■□■□■■□■□■■□■□■■■■■□■□■\n" +
                "□□□□■□■■□■□□□■■□■■■■■■□■■\n" +
                "□■■□■□■■□■□■□■■□■■□■■■□■■\n" +
                "□■■□■■□□■■□■■□■■□□■■■■□■■"
    }
})
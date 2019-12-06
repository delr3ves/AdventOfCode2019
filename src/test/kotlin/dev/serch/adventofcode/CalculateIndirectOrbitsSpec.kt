package dev.serch.adventofcode

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class CalculateIndirectOrbitsSpec : StringSpec({
    val calculateIndirectOrbits = CalculateIndirectOrbits()
    val calculateOrbitalTransfers = CalculateOrbitalTransfers()
    "test sample should match expected" {
        val input = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L)")
        val expected = 42
        calculateIndirectOrbits(input) shouldBe expected
    }

    "test with input should match expected" {
        val input = javaClass.classLoader.getResource("OrbitalInput.txt")
                .readText()
                .split("\n")
        calculateIndirectOrbits(input) shouldBe 224901
    }

    "test day6.2 sample" {
        val input = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN")
        calculateOrbitalTransfers("SAN", "YOU", input) shouldBe 4
    }

    "test day6.2 with input match expected" {
        val input = javaClass.classLoader.getResource("OrbitalTransferInput.txt")
                .readText()
                .split("\n")
        calculateOrbitalTransfers("SAN", "YOU", input) shouldBe 334
    }
})
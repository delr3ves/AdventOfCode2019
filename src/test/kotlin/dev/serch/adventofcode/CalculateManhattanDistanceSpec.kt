package dev.serch.adventofcode

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

internal class CalculateManhattanDistanceSpec : StringSpec({
    val calculateManhattanDistance = CalculateManhattanDistance()

    "test samples should find the solution" {
        forall(
                row(
                        listOf("R8", "U5", "L5", "D3"),
                        listOf("U7", "R6", "D4", "L4"),
                        6
                ),
                row(
                        listOf("R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"),
                        listOf("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83"),
                        159
                ),
                row(
                        listOf("R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51"),
                        listOf("U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7"),
                        135
                )
        ) { wire1, wire2, expected ->
            calculateManhattanDistance(wire1, wire2) shouldBe expected
        }
    }

    "test with input should solve the problem" {
        val input = javaClass.classLoader.getResource("ManhattanCalculatorInput.txt")
                .readText()
                .split("\n")
                .map { it.split(",") }
        calculateManhattanDistance(input[0], input[1]) shouldBe 5319
    }
})
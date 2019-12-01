package dev.serch.adventofcode

import io.kotlintest.data.forall
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class CalculateFuelSpec : StringSpec({
    val calculateFuel = CalculateFuel()

    "calculate fuel should never return a negative amount of fuel" {
        forAll(Gen.long()) { mass ->
            calculateFuel(mass) >= 0
        }
    }

    "test samples should return expected result" {
        forall(
                row(12L, 2L),
                row(14L, 2L),
                row(1969L, 654L),
                row(100756L, 33583L)
        ) { mass, expectedFuel ->
            calculateFuel(mass) shouldBe expectedFuel
        }
    }

    "test with input should solve the first puzzle" {
        val input = javaClass.classLoader.getResource("FuelCalculatorInput.txt")
                .readText()
                .split("\n")
                    .map { it.toLong() }

        calculateFuel(input) shouldBe 3256114L
    }
})

class CalculateFuelIncludingFuelMassSpec : StringSpec({
    val calculateFuel = CalculateFuelIncludingFuelMass()
    "test samples should return expected result" {
        forall(
                row(12L, 2L),
                row(14L, 2L),
                row(1969L, 966L),
                row(100756L, 50346L)
        ) { mass, expectedFuel ->
            calculateFuel(mass) shouldBe expectedFuel
        }
    }

    "test with input should solve the first puzzle" {
        val input = javaClass.classLoader.getResource("FuelCalculatorIncludingExtraFuelInput.txt")
                .readText()
                .split("\n")
                    .map { it.toLong() }

        calculateFuel(input) shouldBe 4881302L
    }
})

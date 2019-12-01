package dev.serch.adventofcode

import kotlin.math.floor
import kotlin.math.max

class CalculateFuel {
    operator fun invoke(mass: Long): Long {
        val thirdOfMass = mass.toDouble() / 3
        return max(0, floor(thirdOfMass).toLong() - 2)
    }

    operator fun invoke(mass: List<Long>): Long {
        return mass.map(::invoke).sum()
    }
}

class CalculateFuelIncludingFuelMass(
    private val calculateFuel: CalculateFuel = CalculateFuel()
) {

    operator fun invoke(mass: Long): Long {
        tailrec fun calculateFuelRec(fuel: Long, acc: Long): Long {
            return if (fuel <= 0) {
                acc
            } else {
                val calculatedFuel = calculateFuel(fuel)
                calculateFuelRec(calculatedFuel, acc + calculatedFuel)
            }
        }
        return calculateFuelRec(mass, 0)
    }

    operator fun invoke(mass: List<Long>): Long {
        return mass.map(::invoke).sum()
    }
}
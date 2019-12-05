package dev.serch.adventofcode

import kotlin.math.absoluteValue

class CalculateManhattanDistance {

    operator fun invoke(wire1: List<String>, wire2: List<String>): Int {
        val pathWire1 = calculatePath(wire1)
        val pathWire2 = calculatePath(wire2)
        val crossPoints = pathWire1.intersect(pathWire2)
        val manhattanDistances = crossPoints.map { it.x.absoluteValue + it.y.absoluteValue }
        return manhattanDistances.min() ?: 0
    }

    fun takingTimeIntoAccount(wire1: List<String>, wire2: List<String>): Int {
        val pathWire1 = calculatePath(wire1)
        val pathWire2 = calculatePath(wire2)
        val crossPoints = pathWire1.intersect(pathWire2)
        val distancesToIntersections = crossPoints.map {
            calculateStepsToAchievePosition(pathWire1, it) + calculateStepsToAchievePosition(pathWire2, it)
        }
        return distancesToIntersections.min() ?: 0
    }

    private fun calculateStepsToAchievePosition(pathWire: List<Point>, point: Point): Int {
        return pathWire.indexOf(point) + 1
    }

    private fun calculatePath(wire: List<String>): List<Point> {
        val centralPoint = Point(0, 0)
        return wire.fold(emptyList()) { acc, currentMovement ->
            val referencePoint = acc.lastOrNull() ?: centralPoint
            acc + move(currentMovement, referencePoint)
        }
    }

    private fun move(movement: String, referencePoint: Point): List<Point> {
        val movementCode = movement.first()
        val times = movement.drop(1).toInt()

        val movementMethod = when (movementCode.toUpperCase()) {
            'U' -> referencePoint::moveUp
            'D' -> referencePoint::moveDown
            'L' -> referencePoint::moveLeft
            'R' -> referencePoint::moveRight
            else -> referencePoint::dontMove
        }
        return (1..times).map {
            movementMethod(it)
        }
    }
}

data class Point(val x: Int, val y: Int) {
    fun moveUp(steps: Int): Point {
        return copy(x, y + steps)
    }

    fun moveDown(steps: Int): Point {
        return copy(x, y - steps)
    }

    fun moveLeft(steps: Int): Point {
        return copy(x - steps, y)
    }

    fun moveRight(steps: Int): Point {
        return copy(x + steps, y)
    }

    fun dontMove(steps: Int): Point {
        return this
    }
}
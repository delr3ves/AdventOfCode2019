package dev.serch.adventofcode

class ParseOrbits {
    operator fun invoke(orbits: List<String>): List<Pair<String, String>> {
        return orbits.map {
            val planets = it.split(')')
            Pair(planets[0], planets[1])
        }
    }
}

class CalculateIndirectOrbits(private val parseOrbits: ParseOrbits = ParseOrbits()) {

    operator fun invoke(orbits: List<String>): Int {
        val orbitsAsPair = parseOrbits(orbits)
        tailrec fun calculate(currentPlanets: List<String>, orbitLevel: Int, orbits: List<Pair<String, String>>, acc: Int, satellitesToProcessNext: List<String>): Int {
            return if (currentPlanets.isEmpty() && satellitesToProcessNext.isEmpty()) {
                acc
            } else if (currentPlanets.isEmpty() && satellitesToProcessNext.isNotEmpty()) {
                calculate(satellitesToProcessNext, orbitLevel + 1, orbits, acc, emptyList())
            } else { // have planets to process
                val nextPlanet = currentPlanets.first()
                val restPlanets = currentPlanets.drop(1)
                val satellitesOfCurrentPlanet = orbits.filter { it.first == nextPlanet }.map { it.second }
                val nonProcessedOrbits = orbits.filter { it.first != nextPlanet }
                calculate(restPlanets, orbitLevel, nonProcessedOrbits, acc + orbitLevel, satellitesToProcessNext + satellitesOfCurrentPlanet)
            }
        }
        return calculate(listOf("COM"), 0, orbitsAsPair, 0, emptyList())
    }
}

class CalculateOrbitalTransfers(private val parseOrbits: ParseOrbits = ParseOrbits()) {
    companion object {
        const val CENTER_OF_THE_WORLD = "COM"
    }

    operator fun invoke(from: String, to: String, orbits: List<String>): Int {
        val orbitsAsPair = parseOrbits(orbits)

        val pathFromFromAndOrigin = createPathFrom(CENTER_OF_THE_WORLD, from, orbitsAsPair)
        val pathFromToAndOrigin = createPathFrom(CENTER_OF_THE_WORLD, to, orbitsAsPair)
        val commonPlanets = pathFromToAndOrigin.intersect(pathFromFromAndOrigin)
        if (commonPlanets.isEmpty()) {
            return 0
        } else {
            val commonPlanet = commonPlanets.first()
            return pathFromToAndOrigin.indexOf(commonPlanet) + pathFromFromAndOrigin.indexOf(commonPlanet) - 2
        }
    }

    private fun createPathFrom(from: String, to: String, orbits: List<Pair<String, String>>): List<String> {
        tailrec fun findPath(from: String, to: String, orbits: List<Pair<String, String>>, acc: List<String>): List<String> {
            if (from == to) {
                return acc + from
            }
            val planetOfCurrentSatellite = orbits.find { it.second == to }
            return if (planetOfCurrentSatellite == null) {
                acc
            } else {
                findPath(from, planetOfCurrentSatellite.first, orbits, acc + planetOfCurrentSatellite.second)
            }
        }

        return findPath(from, to, orbits, emptyList())
    }
}
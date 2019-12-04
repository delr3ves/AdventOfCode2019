package dev.serch.adventofcode

class CalculatePasswordCandidatesByCriteria(private val ensureAdjacentNumbers: EnsureAdjacentNumbers) {
    operator fun invoke(range: IntRange): Int {
        return range.filter {
            hasSixDigit(it) && ensureAdjacentNumbers(it) && dontDecrease(it)
        }.size
    }

    private fun hasSixDigit(candidate: Int): Boolean {
        return candidate.toString().length == 6
    }

    private fun dontDecrease(candidate: Int): Boolean {
        tailrec fun findDecrease(candidate: Int, current: Int = 9): Boolean {
            if (candidate <= 0) {
                return true
            }
            val nextDigit = candidate % 10
            return nextDigit <= current && findDecrease(candidate / 10, nextDigit)
        }

        return findDecrease(candidate)
    }
}

interface EnsureAdjacentNumbers {
    operator fun invoke(number: Int): Boolean
}

class EnsureAtLeastTwoAdjacentNumbers : EnsureAdjacentNumbers {
    override operator fun invoke(candidate: Int): Boolean {
        tailrec fun findAdjacent(candidate: Int, current: Int?): Boolean {
            if (candidate <= 0) {
                return false
            }
            val nextDigit = candidate % 10
            return nextDigit == current || findAdjacent(candidate / 10, nextDigit)
        }
        return findAdjacent(candidate, null)
    }
}

class EnsureFixedNumberOfAdjacentNumbers(private val allowedAdjacentNumbers: Int = 2) : EnsureAdjacentNumbers {
    override operator fun invoke(candidate: Int): Boolean {
        tailrec fun findAdjacent(candidate: Int, current: Int?, currentAdjacentNumbers: Int): Boolean {
            if (candidate <= 0) {
                return currentAdjacentNumbers == allowedAdjacentNumbers
            }
            val nextDigit = candidate % 10
            val newCandidate = candidate / 10
            if (nextDigit == current) {
                return findAdjacent(newCandidate, nextDigit, currentAdjacentNumbers + 1)
            }
            return allowedAdjacentNumbers == currentAdjacentNumbers || findAdjacent(newCandidate, nextDigit, 1)
        }
        return findAdjacent(candidate, null, 0)
    }
}
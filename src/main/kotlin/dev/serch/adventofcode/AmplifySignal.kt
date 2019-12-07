package dev.serch.adventofcode

class AmplifySignal(
    private val processIntCode: ProcessIntCode = ProcessIntCode(),
    private val generateCombinations: CalculatePermutations = CalculatePermutations()
) {

    operator fun invoke(program: List<Int>, amplifiersPhase: List<Int>): Int {
        val executed = amplifiersPhase.fold(ExecutionResult(program, 0)) { acc, phase ->
            processIntCode(acc.program, listOf(phase, acc.output ?: 0))
        }
        return executed.output ?: -1
    }

    operator fun invoke(program: List<Int>, amplifiers: Int = 5): Int {
        val phases = generateCombinations((0..amplifiers - 1).toList())
        return phases.map { this(program, it) }.max() ?: -1
    }
}

class CalculatePermutations {
    tailrec operator fun invoke(list: List<Int>): List<List<Int>> {
        if (list.size == 1) return listOf(list)
        val perms = mutableListOf<List<Int>>()
        val sub = list[0]
        for (perm in this(list.drop(1)))
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, sub)
                perms.add(newPerm)
            }
        return perms
    }
}

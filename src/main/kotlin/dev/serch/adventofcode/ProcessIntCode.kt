package dev.serch.adventofcode

import dev.serch.adventofcode.ProcessIntCode.Companion.replace

class ProcessIntCode {

    operator fun invoke(intCode: List<Int>): List<Int> {
        return process(intCode, 0)
    }

    private tailrec fun process(intCode: List<Int>, instructionPointer: Int): List<Int> {
        val opCode = intCode[instructionPointer]
        val processed = when (opCode) {
            SUM_CODE ->
                Triple(sum(intCode, instructionPointer + 1), true, instructionPointer + 4)
            MULTIPLY_CODE ->
                Triple(multiply(intCode, instructionPointer + 1), true, instructionPointer + 4)
            STOP_CODE ->
                Triple(intCode, false, instructionPointer)
            else ->
                Triple(intCode, false, instructionPointer)
        }

        return if (!processed.second || processed.third >= intCode.size) {
            processed.first
        } else {
            process(processed.first, processed.third)
        }
    }

    private fun sum(intCode: List<Int>, index: Int): List<Int> {
        return operateAndReplace(intCode, index, { a, b -> a + b })
    }

    private fun multiply(intCode: List<Int>, index: Int): List<Int> {
        return operateAndReplace(intCode, index, { a, b -> a * b })
    }

    private fun operateAndReplace(intCode: List<Int>, index: Int, operation: (Int, Int) -> Int): List<Int> {
        val a = intCode[index]
        val b = intCode[index + 1]
        val dest = intCode[index + 2]
        return intCode.replace(dest, operation(intCode[a], intCode[b]))
    }

    companion object {
        const val SUM_CODE = 1
        const val MULTIPLY_CODE = 2
        const val STOP_CODE = 99

        fun <E> Iterable<E>.replace(index: Int, elem: E) = mapIndexed { i, existing -> if (i == index) elem else existing }
    }
}

class FindNounAndVerb(private val processIntCode: ProcessIntCode = ProcessIntCode()) {
    operator fun invoke(opCode: List<Int>, expected: Int): Int? {
        val allowedValues = 0..99
        allowedValues.forEach { noun ->
            allowedValues.forEach { verb ->
                val input1202 = opCode.replace(1, noun).replace(2, verb)
                if (processIntCode(input1202).first() == expected) {
                    return noun * 100 + verb
                }
            }
        }
        return null
    }
}
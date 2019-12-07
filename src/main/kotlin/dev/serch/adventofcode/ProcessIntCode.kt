package dev.serch.adventofcode

import dev.serch.adventofcode.ProcessIntCode.Companion.replace

data class ExecutionResult(val program: List<Int>, val output: Int?)
data class PartialResult(val result: ExecutionResult, val continueProcessing: Boolean, val nextPointer: Int, val nextInput: List<Int>)

class ProcessIntCode {

    operator fun invoke(intCode: List<Int>, input: Int = 0): ExecutionResult {
        return this(intCode, listOf(input))
    }

    operator fun invoke(intCode: List<Int>, input: List<Int>): ExecutionResult {
        return process(ExecutionResult(intCode, null), 0, input)
    }

    private tailrec fun process(currentExecution: ExecutionResult, instructionPointer: Int, input: List<Int>): ExecutionResult {
        val intCode = currentExecution.program
        val instruction = intCode[instructionPointer]
        val opCode = instruction % 100
        val op1Mode = (instruction / 100) % 10
        val op2Mode = (instruction / 1000) % 10

        val processed = when (opCode) {
            SUM_CODE ->
                PartialResult(sum(currentExecution, instructionPointer + 1, op1Mode, op2Mode), true, instructionPointer + 4, input)
            MULTIPLY_CODE ->
                PartialResult(multiply(currentExecution, instructionPointer + 1, op1Mode, op2Mode), true, instructionPointer + 4, input)
            INPUT_CODE ->
                PartialResult(copyInput(currentExecution, instructionPointer + 1, input.first()), true, instructionPointer + 2, input.drop(1) + listOf(input.first()))
            OUTPUT_CODE ->
                PartialResult(copyOutput(intCode, instructionPointer + 1, op1Mode), true, instructionPointer + 2, input)
            JUMP_IF_TRUE_CODE ->
                PartialResult(currentExecution, true, jumpIfTrue(intCode, instructionPointer + 1, op1Mode, op2Mode), input)
            JUMP_IF_FALSE_CODE ->
                PartialResult(currentExecution, true, jumpIfFalse(intCode, instructionPointer + 1, op1Mode, op2Mode), input)
            LESS_THAN_CODE ->
                PartialResult(lessThan(currentExecution, instructionPointer + 1, op1Mode, op2Mode), true, instructionPointer + 4, input)
            EQUALS_CODE ->
                PartialResult(equal(currentExecution, instructionPointer + 1, op1Mode, op2Mode), true, instructionPointer + 4, input)
            STOP_CODE ->
                PartialResult(currentExecution, false, instructionPointer, input)
            else ->
                PartialResult(currentExecution, false, instructionPointer, input)
        }

        return if (!processed.continueProcessing || processed.nextPointer >= intCode.size) {
            processed.result
        } else {
            process(processed.result, processed.nextPointer, processed.nextInput)
        }
    }

    private fun sum(intCode: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int): ExecutionResult {
        return operateAndReplace(intCode, index, op1Mode, op2Mode) { a, b -> a + b }
    }

    private fun multiply(intCode: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int): ExecutionResult {
        return operateAndReplace(intCode, index, op1Mode, op2Mode) { a, b -> a * b }
    }

    private fun copyInput(currentExecution: ExecutionResult, index: Int, input: Int): ExecutionResult {
        val intCode = currentExecution.program
        val a = intCode[index]
        return currentExecution.copy(program = intCode.replace(a, input))
    }

    private fun copyOutput(intCode: List<Int>, index: Int, opMode: Int): ExecutionResult {
        val a = getEffectiveValue(intCode[index], opMode, intCode)
        return ExecutionResult(intCode, a)
    }

    private fun operateAndReplace(currentExecution: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int, operation: (Int, Int) -> Int): ExecutionResult {
        val intCode = currentExecution.program
        val a = getEffectiveValue(intCode[index], op1Mode, intCode)
        val b = getEffectiveValue(intCode[index + 1], op2Mode, intCode)
        val dest = intCode[index + 2]
        return currentExecution.copy(program = intCode.replace(dest, operation(a, b)))
    }

    private fun jumpIfTrue(intCode: List<Int>, index: Int, op1Mode: Int, op2Mode: Int): Int {
        return jumpIf(intCode, index, op1Mode, op2Mode) { it != 0 }
    }

    private fun jumpIfFalse(intCode: List<Int>, index: Int, op1Mode: Int, op2Mode: Int): Int {
        return jumpIf(intCode, index, op1Mode, op2Mode) { it == 0 }
    }

    private fun jumpIf(intCode: List<Int>, index: Int, op1Mode: Int, op2Mode: Int, condition: (Int) -> Boolean): Int {
        val conditionValue = getEffectiveValue(intCode[index], op1Mode, intCode)
        val pointer = getEffectiveValue(intCode[index + 1], op2Mode, intCode)
        return if (condition(conditionValue)) {
            pointer
        } else {
            index + 2
        }
    }

    private fun lessThan(currentExecution: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int): ExecutionResult {
        return conditional(currentExecution, index, op1Mode, op2Mode) { a, b -> a < b }
    }

    private fun equal(currentExecution: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int): ExecutionResult {
        return conditional(currentExecution, index, op1Mode, op2Mode) { a, b -> a == b }
    }

    private fun conditional(currentExecution: ExecutionResult, index: Int, op1Mode: Int, op2Mode: Int, condition: (Int, Int) -> Boolean): ExecutionResult {
        val intCode = currentExecution.program
        val a = getEffectiveValue(intCode[index], op1Mode, intCode)
        val b = getEffectiveValue(intCode[index + 1], op2Mode, intCode)
        val c = intCode[index + 2]
        val newProgram = if (condition(a, b)) {
            intCode.replace(c, 1)
        } else {
            intCode.replace(c, 0)
        }
        return currentExecution.copy(program = newProgram)
    }

    private fun getEffectiveValue(value: Int, opCode: Int, program: List<Int>): Int {
        return if (opCode == 0) {
            program[value]
        } else {
            value
        }
    }

    companion object {
        const val SUM_CODE = 1
        const val MULTIPLY_CODE = 2
        const val INPUT_CODE = 3
        const val OUTPUT_CODE = 4
        const val JUMP_IF_TRUE_CODE = 5
        const val JUMP_IF_FALSE_CODE = 6
        const val LESS_THAN_CODE = 7
        const val EQUALS_CODE = 8
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
                if (processIntCode(input1202).program.first() == expected) {
                    return noun * 100 + verb
                }
            }
        }
        return null
    }
}

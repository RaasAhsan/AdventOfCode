import java.util.*
import kotlin.math.pow
import kotlin.system.exitProcess

object Day7 {

    val puzzle = """
        3,8,1001,8,10,8,105,1,0,0,21,42,67,76,89,110,191,272,353,434,99999,3,9,102,2,9,9,1001,9,2,9,1002,9,2,9,1001,9,2,9,4,9,99,3,9,1001,9,4,9,102,4,9,9,101,3,9,9,1002,9,2,9,1001,9,4,9,4,9,99,3,9,102,5,9,9,4,9,99,3,9,1001,9,3,9,1002,9,3,9,4,9,99,3,9,102,3,9,9,101,2,9,9,1002,9,3,9,101,5,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99
    """.trimIndent()

    fun permutations(elements: List<Int>): List<List<Int>> {
        if (elements.isEmpty() || elements.size == 1) {
            return listOf(elements)
        }

        return elements.map { x ->
            permutations(elements.filter { y -> y != x }).map { listOf(x) + it }
        }.flatten()
    }

    fun run() {
        val code = puzzle
            .split(",")
            .map { it.toInt() }

        var max = 0
        for (phases in permutations(listOf(5, 6, 7, 8, 9))) {
            val programs = mutableListOf(
                Process(code),
                Process(code),
                Process(code),
                Process(code),
                Process(code)
            )

            for (p in 0..4) {
                println("$p piped to ${(p + 1) % 5}")
                programs[p].pipe(programs[(p + 1) % 5])
                programs[p].input.add(phases[p])
            }

            val output = ArrayDeque<Int>()
            programs[0].input.add(0)
            programs[4].output.add(output)

            var done = false
            while (!done) {
                for (p in 0..4) {
                    val res = programs[p].run()
                    if (p == 4 && res == Exit) {
                        println("Exit")
                        done = true
                    }
                }
            }

            if (output.last > max) {
                max = output.last
            }
        }

        println(max)
    }


    open class Run
    object Exit : Run()
    object Yield : Run()

    const val OPCODE_ADD = 1
    const val OPCODE_MUL = 2
    const val OPCODE_READ = 3
    const val OPCODE_WRITE = 4
    const val OPCODE_JMP_IF_TRUE = 5
    const val OPCODE_JMP_IF_FALSE = 6
    const val OPCODE_LESS_THAN = 7
    const val OPCODE_EQUALS = 8
    const val OPCODE_EXIT = 99

    class Process(code: List<Int>) {

        private val program: MutableList<Int> = code.toMutableList()
        private var pc = 0
        private var done = false
        var input: ArrayDeque<Int> = ArrayDeque()
        var output: MutableList<ArrayDeque<Int>> = mutableListOf()

        fun pipe(that: Process) {
            output.add(that.input)
        }

        fun run(): Run {
            if (done) {
                return Exit
            }

            var yielded = false
            while (!done && !yielded) {
                val instruction = program[pc]
                val opcode = instruction % 100
                when (opcode) {
                    OPCODE_ADD -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)
                        val out = program[pc + 3]
                        program[out] = p1 + p2
                        pc += 4
                    }
                    OPCODE_MUL -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)
                        val out = program[pc + 3]
                        program[out] = p1 * p2
                        pc += 4
                    }
                    OPCODE_READ -> {
                        val dest = program[pc + 1]
                        if (input.size == 0) {
                            yielded = true
                        } else {
                            val i = input.remove()
                            program[dest] = i
                            pc += 2
                        }
                    }
                    OPCODE_WRITE -> {
                        val source = program[pc + 1]
                        val o = program[source]
                        output.forEach {
                            it.add(o)
                        }
                        pc += 2
                    }
                    OPCODE_JMP_IF_TRUE -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)

                        if (p1 != 0) {
                            pc = p2
                        } else {
                            pc += 3
                        }
                    }
                    OPCODE_JMP_IF_FALSE -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)

                        if (p1 == 0) {
                            pc = p2
                        } else {
                            pc += 3
                        }
                    }
                    OPCODE_LESS_THAN -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)
                        val p3 = program[pc + 3]

                        if (p1 < p2) {
                            program[p3] = 1
                        } else {
                            program[p3] = 0
                        }

                        pc += 4
                    }
                    OPCODE_EQUALS -> {
                        val p1 = readByMode(pc, 0)
                        val p2 = readByMode(pc, 1)
                        val p3 = program[pc + 3]

                        if (p1 == p2) {
                            program[p3] = 1
                        } else {
                            program[p3] = 0
                        }

                        pc += 4
                    }
                    OPCODE_EXIT -> {
//                    println("Terminated successfully")
                        done = true
                    }
                    else -> {
                        println("Invalid opcode reached: $pc - $instruction")
                        done = true
                    }
                }
            }

            return if (done) {
                Exit
            } else {
                Yield
            }
        }

        // 0 - positional addressing
        // 1 - immediate addressing
        private fun readByMode(pc: Int, n: Int): Int {
            val mode = (program[pc] / 10.0.pow(n + 2).toInt()) % 10
            return when (mode) {
                0 -> program[program[pc + n + 1]]
                1 -> program[pc + n + 1]
                else -> exitProcess(0)
            }
        }
        
    }

}

fun main() {
    Day7.run()
}

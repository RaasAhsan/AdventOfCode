import kotlin.math.pow
import kotlin.system.exitProcess

object Day5 {

    val test = """
        3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9
    """.trimIndent()

    val input = """
        3,225,1,225,6,6,1100,1,238,225,104,0,1002,148,28,224,1001,224,-672,224,4,224,1002,223,8,223,101,3,224,224,1,224,223,223,1102,8,21,225,1102,13,10,225,1102,21,10,225,1102,6,14,225,1102,94,17,225,1,40,173,224,1001,224,-90,224,4,224,102,8,223,223,1001,224,4,224,1,224,223,223,2,35,44,224,101,-80,224,224,4,224,102,8,223,223,101,6,224,224,1,223,224,223,1101,26,94,224,101,-120,224,224,4,224,102,8,223,223,1001,224,7,224,1,224,223,223,1001,52,70,224,101,-87,224,224,4,224,1002,223,8,223,1001,224,2,224,1,223,224,223,1101,16,92,225,1101,59,24,225,102,83,48,224,101,-1162,224,224,4,224,102,8,223,223,101,4,224,224,1,223,224,223,1101,80,10,225,101,5,143,224,1001,224,-21,224,4,224,1002,223,8,223,1001,224,6,224,1,223,224,223,1102,94,67,224,101,-6298,224,224,4,224,102,8,223,223,1001,224,3,224,1,224,223,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,108,677,677,224,102,2,223,223,1005,224,329,101,1,223,223,1107,677,226,224,102,2,223,223,1006,224,344,101,1,223,223,1107,226,226,224,102,2,223,223,1006,224,359,101,1,223,223,1108,677,677,224,102,2,223,223,1005,224,374,101,1,223,223,8,677,226,224,1002,223,2,223,1005,224,389,101,1,223,223,108,226,677,224,1002,223,2,223,1006,224,404,1001,223,1,223,107,677,677,224,102,2,223,223,1006,224,419,101,1,223,223,1007,226,226,224,102,2,223,223,1005,224,434,101,1,223,223,1007,677,677,224,102,2,223,223,1005,224,449,1001,223,1,223,8,677,677,224,1002,223,2,223,1006,224,464,101,1,223,223,1108,677,226,224,1002,223,2,223,1005,224,479,101,1,223,223,7,677,226,224,1002,223,2,223,1005,224,494,101,1,223,223,1008,677,677,224,1002,223,2,223,1006,224,509,1001,223,1,223,1007,226,677,224,1002,223,2,223,1006,224,524,1001,223,1,223,107,226,226,224,1002,223,2,223,1006,224,539,1001,223,1,223,1107,226,677,224,102,2,223,223,1005,224,554,101,1,223,223,1108,226,677,224,102,2,223,223,1006,224,569,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,584,1001,223,1,223,7,226,226,224,1002,223,2,223,1006,224,599,101,1,223,223,8,226,677,224,102,2,223,223,1005,224,614,101,1,223,223,7,226,677,224,1002,223,2,223,1005,224,629,101,1,223,223,1008,226,677,224,1002,223,2,223,1006,224,644,101,1,223,223,107,226,677,224,1002,223,2,223,1005,224,659,1001,223,1,223,1008,226,226,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226
    """.trimIndent()

    fun run() {
        val code = input
            .split(",")
            .map { it.toInt() }

        println(interpret(code))
    }

    fun interpret(code: List<Int>): List<Int> {
        val program = code.toMutableList()

        var pc = 0
        var done = false
        while (!done) {
            val value = program[pc]
            val opcode = value % 100
            when (opcode) {
                1 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)
                    val out = program[pc + 3]
                    program[out] = p1 + p2
                    pc += 4
                }
                2 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)
                    val out = program[pc + 3]
                    program[out] = p1 * p2
                    pc += 4
                }
                3 -> {
                    val dest = program[pc + 1]
                    println("Enter a number: ")
                    val input = readLine()!!.toInt()
                    program[dest] = input
                    pc += 2
                }
                4 -> {
                    val source = program[pc + 1]
                    val output = program[source]
                    println("Output: $output")
                    pc += 2
                }
                5 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)

                    if (p1 != 0) {
                        pc = p2
                    } else {
                        pc += 3
                    }
                }
                6 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)

                    if (p1 == 0) {
                        pc = p2
                    } else {
                        pc += 3
                    }
                }
                7 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)
                    val p3 = program[pc + 3]

                    if (p1 < p2) {
                        program[p3] = 1
                    } else {
                        program[p3] = 0
                    }

                    pc += 4
                }
                8 -> {
                    val p1 = readByMode(program, pc, 0)
                    val p2 = readByMode(program, pc, 1)
                    val p3 = program[pc + 3]

                    if (p1 == p2) {
                        program[p3] = 1
                    } else {
                        program[p3] = 0
                    }

                    pc += 4
                }
                99 -> {
//                    println("Terminated successfully")
                    done = true
                }
                else -> {
                    println("Invalid opcode reached: $pc - $value")
                    done = true
                }
            }
        }
        return program
    }

    // 0 - positional addressing
    // 1 - immediate addressing
    fun readByMode(program: List<Int>, pc: Int, n: Int): Int {
        val mode = (program[pc] / 10.0.pow(n + 2).toInt()) % 10
        return when (mode) {
            0 -> program[program[pc + n + 1]]
            1 -> program[pc + n + 1]
            else -> exitProcess(0)
        }
    }

}

fun main() {
    Day5.run()
}

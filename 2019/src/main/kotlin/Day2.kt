object Day2 {

    val input = """
        1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,6,19,1,9,19,23,1,6,23,27,1,10,27,31,1,5,31,35,2,6,35,39,1,5,39,43,1,5,43,47,2,47,6,51,1,51,5,55,1,13,55,59,2,9,59,63,1,5,63,67,2,67,9,71,1,5,71,75,2,10,75,79,1,6,79,83,1,13,83,87,1,10,87,91,1,91,5,95,2,95,10,99,2,9,99,103,1,103,6,107,1,107,10,111,2,111,10,115,1,115,6,119,2,119,9,123,1,123,6,127,2,127,10,131,1,131,6,135,2,6,135,139,1,139,5,143,1,9,143,147,1,13,147,151,1,2,151,155,1,10,155,0,99,2,14,0,0
    """.trimIndent()

    fun run() {
        val code = input
            .split(",")
            .map { it.toInt() }

//        val program = code.toMutableList()
//        program[1] = 12
//        program[2] = 2
//
//        println(interpret(program))

        for (i in 0 until 100) {
            for (j in 0 until 100) {
                val program = code.toMutableList()
                program[1] = i
                program[2] = j
                val output = interpret(program)
                if (output[0] == 19690720) {
                    println("Found: ${100 * i + j}")
                    break
                }
            }
        }
    }

    fun interpret(code: List<Int>): List<Int> {
        val program = code.toMutableList()

        var pc = 0
        var done = false
        while (!done) {
            when (program[pc]) {
                1 -> {
                    val lhs = program[program[pc + 1]]
                    val rhs = program[program[pc + 2]]
                    val out = program[pc + 3]
                    program[out] = lhs + rhs
                    pc += 4
                }
                2 -> {
                    val lhs = program[program[pc + 1]]
                    val rhs = program[program[pc + 2]]
                    val out = program[pc + 3]
                    program[out] = lhs * rhs
                    pc += 4
                }
                99 -> {
//                    println("Terminated successfully")
                    done = true
                }
                else -> {
                    println("Invalid opcode reached")
                    done = true
                }
            }
        }
        return program
    }

}

fun main() {
    Day2.run()
}

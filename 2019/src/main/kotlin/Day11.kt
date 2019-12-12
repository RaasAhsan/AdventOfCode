import java.util.*
import kotlin.math.pow
import kotlin.system.exitProcess

object Day11 {

    val puzzle = """
        3,8,1005,8,311,1106,0,11,0,0,0,104,1,104,0,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,1002,8,1,28,2,103,7,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,55,2,3,6,10,1,101,5,10,1,6,7,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,0,10,4,10,1001,8,0,89,1,1108,11,10,2,1002,13,10,1006,0,92,1,2,13,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,126,3,8,1002,8,-1,10,101,1,10,10,4,10,108,1,8,10,4,10,1002,8,1,147,1,7,0,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,173,1006,0,96,3,8,102,-1,8,10,101,1,10,10,4,10,108,0,8,10,4,10,1001,8,0,198,1,3,7,10,1006,0,94,2,1003,20,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,232,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,253,1006,0,63,1,109,16,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,101,0,8,283,2,1107,14,10,1,105,11,10,101,1,9,9,1007,9,1098,10,1005,10,15,99,109,633,104,0,104,1,21102,837951005592,1,1,21101,328,0,0,1105,1,432,21101,0,847069840276,1,21101,0,339,0,1106,0,432,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21102,179318123543,1,1,21102,386,1,0,1106,0,432,21102,1,29220688067,1,21102,1,397,0,1106,0,432,3,10,104,0,104,0,3,10,104,0,104,0,21102,709580567396,1,1,21102,1,420,0,1105,1,432,21102,1,868498694912,1,21102,431,1,0,1106,0,432,99,109,2,22101,0,-1,1,21101,40,0,2,21101,0,463,3,21101,0,453,0,1105,1,496,109,-2,2106,0,0,0,1,0,0,1,109,2,3,10,204,-1,1001,458,459,474,4,0,1001,458,1,458,108,4,458,10,1006,10,490,1102,1,0,458,109,-2,2105,1,0,0,109,4,1202,-1,1,495,1207,-3,0,10,1006,10,513,21102,0,1,-3,21201,-3,0,1,21202,-2,1,2,21101,0,1,3,21101,0,532,0,1106,0,537,109,-4,2106,0,0,109,5,1207,-3,1,10,1006,10,560,2207,-4,-2,10,1006,10,560,22102,1,-4,-4,1105,1,628,21201,-4,0,1,21201,-3,-1,2,21202,-2,2,3,21101,0,579,0,1105,1,537,22101,0,1,-4,21102,1,1,-1,2207,-4,-2,10,1006,10,598,21102,1,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,620,22102,1,-1,1,21101,0,620,0,106,0,495,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2106,0,0
    """.trimIndent()

    const val BLACK = 0
    const val WHITE = 1

    fun run() {
        val code = puzzle
            .split(",")
            .map { it.toLong() }

        val program = Process(code)
        val output = ArrayDeque<Long>()
        program.output.add(output)

        val grid = mutableMapOf<Pair<Int, Int>, Int>()
        var x = 0
        var y = 0
        var direction = 0

        grid[Pair(0, 0)] = WHITE

        var done = false
        while (!done) {
            program.input.add(grid.getOrDefault(Pair(x, y), BLACK).toLong())

            val res = program.run()
            when (res) {
                Exit -> {
                    println("Exit")
                    done = true
                }
                Yield -> {
                    val color = output.remove()
                    val turn = output.remove()

                    grid[Pair(x, y)] = color.toInt()
                    if (turn == 0L) {
                        direction -= 1
                    } else {
                        direction += 1
                    }

                    if (direction < 0) {
                        direction += 4
                    }
                    if (direction > 3) {
                        direction -= 4
                    }

                    when (direction) {
                        0 -> y -= 1
                        1 -> x += 1
                        2 -> y += 1
                        3 -> x -= 1
                    }
                }
            }
        }

        println(grid.keys.size)

        val maxX = grid.keys.toList().map { it.first }.max()!!
        val maxY = grid.keys.toList().map { it.second }.max()!!
        val width = maxX + 1
        val height = maxY + 1

        val render = LongArray(width * height)
        grid.toList().forEach { entry ->
            val ex = entry.first.first
            val ey = entry.first.second
            render[ey * width + ex] = entry.second.toLong()
        }


        for (ey in 0 until height) {
            for (ex in 0 until width) {
                if (render[ey * width + ex] == 1L) {
                    print('X')
                } else {
                    print(' ')
                }
            }
            println()
        }
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
    const val OPCODE_ADJUST_RELBASE = 9
    const val OPCODE_EXIT = 99

    class Process(code: List<Long>) {

        private var pc: Int = 0
        private var memory: Memory = Memory()
        private var relbase: Long = 0
        private var done = false
        var input: ArrayDeque<Long> = ArrayDeque()
        var output: MutableList<ArrayDeque<Long>> = mutableListOf()

        init {
            memory.writeMany(0, code)
        }

        fun pipe(that: Process) {
            output.add(that.input)
        }

        fun run(): Run {
            if (done) {
                return Exit
            }

            var yielded = false
            while (!done && !yielded) {
                val instruction = memory.read(pc)
                val opcode = (instruction % 100).toInt()
                when (opcode) {
                    OPCODE_ADD -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)
                        writeValueByMode(pc, 2, p1 + p2)
                        pc += 4
                    }
                    OPCODE_MUL -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)
                        writeValueByMode(pc, 2, p1 * p2)
                        pc += 4
                    }
                    OPCODE_READ -> {
                        if (input.size == 0) {
                            yielded = true
                        } else {
                            val i = input.remove()
                            writeValueByMode(pc, 0, i)
                            pc += 2
                        }
                    }
                    OPCODE_WRITE -> {
                        val value = readValueByMode(pc, 0)
                        output.forEach {
                            it.add(value)
                        }
                        pc += 2
                    }
                    OPCODE_JMP_IF_TRUE -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)

                        if (p1 != 0L) {
                            pc = p2.toInt()
                        } else {
                            pc += 3
                        }
                    }
                    OPCODE_JMP_IF_FALSE -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)

                        if (p1 == 0L) {
                            pc = p2.toInt()
                        } else {
                            pc += 3
                        }
                    }
                    OPCODE_LESS_THAN -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)

                        writeValueByMode(pc, 2, if (p1 < p2) 1 else 0)

                        pc += 4
                    }
                    OPCODE_EQUALS -> {
                        val p1 = readValueByMode(pc, 0)
                        val p2 = readValueByMode(pc, 1)

                        writeValueByMode(pc, 2, if (p1 == p2) 1 else 0)

                        pc += 4
                    }
                    OPCODE_ADJUST_RELBASE -> {
                        val p1 = readValueByMode(pc, 0)
                        relbase += p1
                        pc += 2
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

        private fun readValueByMode(pc: Int, n: Int): Long {
            return memory.read(readAddress(pc, n))
        }

        private fun writeValueByMode(pc: Int, n: Int, value: Long) {
            memory.write(readAddress(pc, n), value)
        }

        // 0 - positional addressing
        // 1 - immediate addressing
        // 2 - relative addressing
        private fun readAddress(pc: Int, n: Int): Int {
            val mode = (memory.read(pc) / 10.0.pow(n + 2).toInt()) % 10
            return when (mode) {
                0L -> memory.read(pc + n + 1).toInt()
                1L -> pc + n + 1
                2L -> (relbase + memory.read(pc + n + 1)).toInt()
                else -> exitProcess(0)
            }
        }

    }

    // TODO: different memory implementations
    class Memory {

        private val pageSize = 100
        private val memorySize = pageSize * 100

        private val pages = mutableMapOf<Int, Int>()
        private val array = LongArray(memorySize)
        private val freeSpace = mutableListOf<Int>()

        init {
            for (i in 0 until memorySize step pageSize) {
                freeSpace.add(i)
            }
        }

        // pages are aligned to 100 long boundaries
        fun write(address: Int, value: Long) {
            val page = address / pageSize
            val offset = address % pageSize

            val physicalAddress = pages[page]
            if (physicalAddress != null) {
                array[physicalAddress + offset] = value
            } else {
                val newPhysicalAddress = freeSpace.removeAt(freeSpace.size - 1)
                pages[page] = newPhysicalAddress
                array[newPhysicalAddress + offset] = value
            }
        }

        fun writeMany(address: Int, values: List<Long>) {
            for (i in 0 until values.size) {
                write(address + i, values[i])
            }
        }

        fun write(address: Int, value: Int) {
            write(address, value.toLong())
        }

        fun read(address: Int): Long {
            val page = address / pageSize
            val offset = address % pageSize

            val physicalAddress = pages[page]
            if (physicalAddress != null) {
                return array[physicalAddress + offset]
            } else {
                val newPhysicalAddress = freeSpace.removeAt(freeSpace.size - 1)
                pages[page] = newPhysicalAddress
                return array[newPhysicalAddress + offset]
            }
        }

    }

}

fun main() {
    Day11.run()
}

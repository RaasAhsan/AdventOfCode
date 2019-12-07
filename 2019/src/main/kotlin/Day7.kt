
object Day7 {

    val input = """
        
    """.trimIndent()

    fun run() {
        val directOrbits = input
            .split("\n")
            .map { it.split(")") }
            .map { Pair(it[1], it[0]) }


    }

}

fun main() {
    Day7.run()
}
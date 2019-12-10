object Day10 {

    val puzzle = """
...###.#########.####
.######.###.###.##...
####.########.#####.#
########.####.##.###.
####..#.####.#.#.##..
#.################.##
..######.##.##.#####.
#.####.#####.###.#.##
#####.#########.#####
#####.##..##..#.#####
##.######....########
.#######.#.#########.
.#.##.#.#.#.##.###.##
######...####.#.#.###
###############.#.###
#.#####.##..###.##.#.
##..##..###.#.#######
#..#..########.#.##..
#.#.######.##.##...##
.#.##.#####.#..#####.
#.#.##########..#.##.
    """.trimIndent()

    fun gcd(a: Int, b: Int): Int {
        var v = 1
        for (i in 1..(Math.min(Math.abs(a), Math.abs(b)))) {
            if (a % i == 0 && b % i == 0) {
                v = i
            }
        }
        return v
    }

    fun start() {
        val input = puzzle
            .split("\n")

        val asteroids = mutableSetOf<Pair<Int, Int>>()
        var x = 0
        var y = 0
        for (row in input) {
            for (char in row) {
                if (char == '#') {
                  asteroids.add(Pair(x, y))
                }
                x += 1
            }
            y += 1
            x = 0
        }

        var maxAsteroids = 0
        var station = Pair(0, 0)
        var stationSlopes = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
        asteroids.forEach { a ->
            val slopes = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
            asteroids.forEach { b ->
                if (a != b) {
                    val dy = (b.second - a.second)
                    val dx = (b.first - a.first)

                    val slope = when {
                        dx == 0 -> {
                            Pair(dy / Math.abs(dy), 0)
                        }
                        dy == 0 -> {
                            Pair(0, dx / Math.abs(dx))
                        }
                        else -> {
                            val g = gcd(dy, dx)
                            Pair(dy / g, dx / g)
                        }
                    }

                    if (slopes.containsKey(slope)) {
                        slopes[slope]!!.add(Pair(dy, dx))
                    } else {
                        slopes[slope] = mutableListOf(Pair(dy, dx))
                    }
                }
            }

            if (slopes.keys.size > maxAsteroids) {
                maxAsteroids = slopes.keys.size
                station = a
                stationSlopes = slopes
            }
        }

        println(maxAsteroids)
        println(station)

        val ordered = stationSlopes
            .mapValues { it.value.sortedBy { x -> x.first * x.first + x.second * x.second }.toMutableList() }
            .mapKeys {
                val a = Math.atan2(it.key.second.toDouble(), it.key.first.toDouble())
                if (a < 0) {
                    a + 2 * Math.PI
                } else {
                    a
                }
            }
            .toList()
            .sortedBy { it.first }

        println(ordered)

        var i = 0
        ordered.forEach {
            if (it.second.isNotEmpty()) {
                val a = it.second.removeAt(0)
                i += 1
                println("${it.first}: $a, $i")
            }
        }

    }

}

fun main() {
    Day10.start()
}
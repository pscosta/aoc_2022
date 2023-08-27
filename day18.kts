import java.io.File

val points = File("s18.txt").readLines()
    .map { it.split(",").let { (x, y, z) -> Point(x.toInt(), y.toInt(), z.toInt()) } }

data class Point(val x: Int, val y: Int, val z: Int) {
    fun adjacent() = setOf(
        Point(x + 1, y, z), Point(x - 1, y, z),
        Point(x, y + 1, z), Point(x, y - 1, z),
        Point(x, y, z + 1), Point(x, y, z - 1)
    )
}

fun part2(): Int {
    val toCheck = mutableListOf(Point(0, 0, 0))
    val seen = mutableSetOf<Point>()
    val maxX = points.maxOf { it.x } + 1
    val maxY = points.maxOf { it.y } + 1
    val maxZ = points.maxOf { it.z } + 1

    var count = 0
    while (toCheck.isNotEmpty()) {
        when (val cube = toCheck.removeFirst()) {
            in seen -> continue
            else -> cube.adjacent()
                .filter { it.x <= maxX && it.y <= maxY && it.z <= maxZ }
                .filter { it.x >= -1 && it.y >= -1 && it.z >= -1 }
                .forEach { if (it in points) count++ else toCheck.add(it) }
                .also { seen += cube }
        }
    }
    return count
}

fun main() {
    println("part 1: " + points.sumOf { it.adjacent().count { adj -> adj !in points } })
    println("part 2: " + part2())
}

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int)

var maxY = 0
val start = Point(500, 0)
lateinit var cave: HashMap<Int, HashMap<Int, Any>>

fun loadCave() {
    cave = HashMap()
    File("input/14.txt").readLines()
        .map { line ->
            line.split(" -> ")
                .map { it.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) } }
                .windowed(2, 1) { (p1, p2) ->
                    (min(p1.x, p2.x)..max(p1.x, p2.x)).map { x ->
                        (min(p1.y, p2.y)..max(p1.y, p2.y)).map { y ->
                            cave.getOrPut(x) { HashMap() }[y] = "Rock"
                        }
                    }
                }
        }.also { maxY = cave.values.flatMap { it.keys }.max() + 2 }
}

fun isEmpty(x: Int, y: Int) = y < maxY && !(cave[x]?.containsKey(y) ?: false)

tailrec fun dropTo(x: Int, y: Int, floor: Boolean, acc: Int = 0): Int = when {
    floor && y == maxY -> acc
    isEmpty(x, y) -> dropTo(x, y + 1, floor, acc)
    y == start.y -> acc
    isEmpty(x - 1, y) -> dropTo(x - 1, y, floor, acc)
    isEmpty(x + 1, y) -> dropTo(x + 1, y, floor, acc)
    else -> {
        cave.getOrPut(x) { HashMap() }[y - 1] = "Sand"
        dropTo(start.x, start.y, floor, acc + 1)
    }
}

fun main() {
    loadCave().also { println("part 1: " + dropTo(start.x, start.y, true)) }
    loadCave().also { println("part 2: " + dropTo(start.x, start.y, false)) }
}

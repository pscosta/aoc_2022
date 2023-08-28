import java.io.File
import kotlin.math.abs

data class Sensor(val x: Int, val y: Int, val bx: Int, val by: Int) {
    val range = abs(x - bx) + abs(y - by)
    fun covers(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y) <= range
}

fun MatchResult?.ints() = this!!.destructured.toList().map { it.toInt() }
val regex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$")

val sensors = File("input/s15.txt").readLines()
    .map { regex.find(it).ints().let { (x, y, bx, by) -> Sensor(x, y, bx, by) } }

fun part1(y: Int, points: HashSet<Int> = HashSet()): Int {
    sensors.map {
        var x = it.x
        while (true) if (it.covers(x, y)) points.add(x).also { x++ } else break
        x = it.x
        while (true) if (it.covers(x, y)) points.add(x).also { x-- } else break
    }
    return points.count { p -> sensors.none { it.bx == p && it.by == y } }
}

fun part2(max: Int): Long {
    for (y in 0..max) {
        var x = 0
        while (x < max) when (val it = sensors.firstOrNull { it.covers(x, y) }) {
            null -> return x * max.toLong() + y
            else -> x = it.x + it.range - abs(it.y - y) + 1
        }
    }
    throw IllegalStateException()
}

fun main() {
    println("part 1: " + part1(2000000))
    println("part 2: " + part2(4000000))
}
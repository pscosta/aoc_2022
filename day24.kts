import Day24.Dir.*
import java.io.File

enum class Dir { North, South, West, East; }
data class Point(var x: Int, var y: Int) {
    fun adjacent() = listOf(Point(x, y - 1), Point(x, y + 1), Point(x - 1, y), Point(x + 1, y))
    fun isFree() = this !in grid
}

data class Storm(var point: Point, var dir: Dir) {
    fun move(): Point = with(point) {
        point = when (dir) {
            East -> if (x + 1 <= maxX) Point(x + 1, y) else Point(1, y)
            West -> if ((x - 1) > 0) Point(x - 1, y) else Point(maxX, y)
            North -> if ((y - 1) > 0) Point(x, y - 1) else Point(x, maxY)
            South -> if ((y + 1) <= maxY) Point(x, y + 1) else Point(x, 1)
        }
        return point
    }
}

fun load() = File("s24.txt").readLines()
    .flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, s ->
            when (s) {
                '>' -> Point(x, y) to mutableListOf(Storm(Point(x, y), East))
                '<' -> Point(x, y) to mutableListOf(Storm(Point(x, y), West))
                '^' -> Point(x, y) to mutableListOf(Storm(Point(x, y), North))
                'v' -> Point(x, y) to mutableListOf(Storm(Point(x, y), South))
                else -> {
                    if (s == '#') walls.add(Point(x, y))
                    null
                }
            }
        }
    }.toMap()

fun round(targetTrips: Int, start: Point = startPoint, dest: Point = destPoint): Int {
    grid = load()
    var elves = mutableSetOf(start)
    var target = dest
    var trips = 0

    for (round in 1..Int.MAX_VALUE) {
        val newGrid = HashMap<Point, ArrayList<Storm>>()
        grid.values.flatten().map { storm -> newGrid.getOrPut(storm.move()) { ArrayList() }.add(storm) }
        grid = newGrid

        elves = (elves + elves.flatMap { it.adjacent() })
            .filter { it !in walls }
            .filter { it.y >= 0 && it.y <= maxY + 1 }
            .filter { it.isFree() }.toMutableSet()

        if (target in elves) {
            if (++trips == targetTrips) return round else {
                if (target == destPoint) {
                    target = startPoint
                    elves = mutableSetOf(destPoint)
                } else {
                    target = destPoint
                    elves = mutableSetOf(startPoint)
                }
            }
        }
    }
    return -1
}

var walls = mutableSetOf<Point>()
var grid = load()

val maxX = walls.maxBy { it.x }.x - 1
val maxY = walls.maxBy { it.y }.y - 1

val startPoint = Point(1, 0)
val destPoint = Point(maxX, maxY + 1)

fun main() {
    println("part 1: " + round(targetTrips = 1))
    println("part 2: " + round(targetTrips = 3))
}

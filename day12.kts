import java.io.File

data class Point(val x: Int, val y: Int, val h: Int = 0, var steps: Int = 0)

lateinit var start: Point
lateinit var end: Point

fun load() = File("s12.txt")
    .readLines()
    .mapIndexed { y, line ->
        line.mapIndexed { x, p ->
            when (p) {
                'S' -> Point(x, y, 'a'.code - 97).also { start = it }
                'E' -> Point(x, y, 'z'.code - 97).also { end = it }
                else -> Point(x, y, p.code - 97)
            }
        }
    }

var map = load()

fun bfs(start: Point, isEnd: (Point) -> Boolean, isNext: (Point, Point) -> Boolean): Int {
    val path = mutableListOf(start)
    val visited = mutableSetOf<Point>()

    while (path.isNotEmpty()) {
        val p = path.removeFirst()
        when {
            p in visited -> continue
            isEnd(p) -> return p.steps
            else -> listOf(Point(0, 1), Point(-1, 0), Point(0, -1), Point(1, 0))
                .filter { next -> p.x + next.x in map[0].indices && p.y + next.y in map.indices }
                .filter { next -> isNext(next, p) }
                .forEach { next -> path.add(map[p.y + next.y][p.x + next.x].also { it.steps = p.steps + 1 }) }
                .also { visited.add(p) }
        }
    }
    return 0
}

fun main() {
    println("part 1: " + bfs(start, { it == end }, { next, p -> map[p.y + next.y][p.x + next.x].h - p.h <= 1 }))
    map = load()
    println("part 2: " + bfs(end, { it.h == 0 }, { next, p -> p.h - map[p.y + next.y][p.x + next.x].h <= 1 }))
}

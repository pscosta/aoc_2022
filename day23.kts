import Day23.Dir.*
import java.io.File

data class Point(var x: Int, var y: Int)
enum class Dir { North, South, West, East; }

fun List<Point>.allFree() = this.none { it in grid.keys }
fun Dir.nextDir() = Dir.entries[(Dir.entries.indexOf(this) + 1).mod(entries.size)]

data class Elf(var point: Point, var dir: Dir = North) {
    fun isAlone() = Dir.entries.flatMap { adjacent(it) }.allFree()
    fun turn() = run { this.dir = Dir.entries[(Dir.entries.indexOf(dir) + 1).mod(entries.size)] }

    private fun adjacent(dir: Dir) = with(point) {
        when (dir) {
            North -> listOf(Point(x, y - 1), Point(x - 1, y - 1), Point(x + 1, y - 1))
            South -> listOf(Point(x, y + 1), Point(x - 1, y + 1), Point(x + 1, y + 1))
            West -> listOf(Point(x - 1, y), Point(x - 1, y - 1), Point(x - 1, y + 1))
            East -> listOf(Point(x + 1, y), Point(x + 1, y - 1), Point(x + 1, y + 1))
        }
    }

    fun proposeNext(dir: Dir, checkedDirs: Int = 0): Point? = with(point) {
        when {
            checkedDirs == Dir.entries.size -> null
            adjacent(dir).allFree() -> when (dir) {
                North -> Point(x, y - 1)
                South -> Point(x, y + 1)
                West -> Point(x - 1, y)
                East -> Point(x + 1, y)
            }
            else -> proposeNext(dir.nextDir(), checkedDirs + 1)
        }
    }
}

fun round(times: Int = Int.MAX_VALUE): Int {
    grid = load()
    for (round in 1..times) {
        val proposals = grid.map { (_, elf) ->
            when {
                elf.isAlone() -> elf to elf.point
                else -> elf.proposeNext(elf.dir)?.let { elf to it } ?: (elf to elf.point)
            }.also { elf.turn() }
        }

        val dups = proposals.groupingBy { (_, p) -> p }.eachCount().filter { it.value > 1 }.map { it.key }

        val newGrid = proposals.associate { (elf, next) ->
            if (next in dups) elf.point to elf
            else next to (elf.apply { point = next })
        }

        if (grid.keys == newGrid.keys) return round else grid = newGrid
    }
    return -1
}

fun countPoints(): Int {
    val min = Point(grid.keys.minOf { it.x }, grid.keys.minOf { it.y })
    val max = Point(grid.keys.maxOf { it.x }, grid.keys.maxOf { it.y })
    return ((max.x - min.x + 1) * (max.y - min.y + 1)) - grid.size
}

var grid = load()
fun load() = File("input/s23.txt").readLines()
    .flatMapIndexed { y, l -> l.mapIndexedNotNull { x, s -> if (s == '#') Point(x, y) to Elf(Point(x, y)) else null } }
    .toMap()

fun main() {
    round(10).also { println("part 1: " + countPoints()) }
    println("part 2: " + round())
}
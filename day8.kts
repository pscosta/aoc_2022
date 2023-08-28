import java.io.File

fun visible(x: Int, y: Int, grid: List<List<Int>>) = when {
    x == 0 || y == 0 -> true
    x == grid[0].size - 1 || y == grid.size - 1 -> true
    else -> (0 until y).all { grid[it][x] < grid[y][x] } ||
            (y + 1 until grid.size).all { grid[it][x] < grid[y][x] } ||
            (0 until x).all { grid[y][it] < grid[y][x] } ||
            (x + 1 until grid[0].size).all { grid[y][it] < grid[y][x] }
}

fun score(x: Int, y: Int, g: List<List<Int>>) = 1 *
        calc(y - 1 downTo 0) { g[it][x] >= g[y][x] } *
        calc(y + 1 until g.size) { g[it][x] >= g[y][x] } *
        calc(x - 1 downTo 0) { g[y][it] >= g[y][x] } *
        calc(x + 1 until g[0].size) { g[y][it] >= g[y][x] }

fun calc(dir: IntProgression, stop: (Int) -> Boolean) = when (val sum = dir.indexOfFirst(stop)) {
    -1 -> dir.count()
    else -> sum + 1
}

fun main() {
    val grid = File("input/s8.txt")
        .readLines()
        .map { it.map { c -> c.digitToInt() } }

    println("part 1: " + grid.indices.sumOf { y -> grid[y].indices.count { x -> visible(x, y, grid) } })
    println("part 2: " + grid.indices.map { y -> grid[y].indices.map { x -> score(x, y, grid) } }.flatten().max())
}
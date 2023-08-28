import java.io.File
import kotlin.math.abs

data class Move(val dir: String, val steps: Int)
data class Pos(var x: Int, var y: Int)

fun Pos.move(dir: String) = when (dir) {
    "R" -> x++
    "L" -> x--
    "U" -> y++
    else -> y--
}

fun Pos.follow(head: Pos) {
    if (abs(head.x - x) <= 1 && abs(head.y - y) <= 1) return
    if (head.x > x) x++
    if (head.x < x) x--
    if (head.y > y) y++
    if (head.y < y) y--
}

val moves = File("input/s9.txt").readLines()
    .map { it.split(" ").let { (dir, s) -> Move(dir, s.toInt()) } }

fun solve(size: Int, tails: HashSet<Pos> = HashSet()): Int {
    val rope = List(size) { Pos(0, 0) }

    moves.forEach { (dir, steps) ->
        repeat(steps) {
            rope[0].move(dir)
            rope.windowed(2) { (head, tail) -> tail.follow(head) }
            tails.add(rope.last().copy())
        }
    }
    return tails.size
}

fun main() {
    println("part 1: " + solve(2))
    println("part 2: " + solve(10))
}



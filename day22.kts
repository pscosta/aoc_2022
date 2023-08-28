import Day22.Dir.*
import java.io.File

data class Pt(val x: Int, val y: Int)
data class Move(val steps: Int? = null, val dir: Dir? = null)
enum class Dir(val value: Int) {
    Right(0), Down(1), Left(2), Up(3);

    fun turn(dir: Dir): Dir = when (dir) {
        Right -> Dir.entries[(entries.indexOf(this) + 1).mod(entries.size)]
        else -> Dir.entries[(entries.indexOf(this) - 1).mod(entries.size)]
    }
}

val input = File("input/s22.txt").readLines()
val path = input.dropLast(2)
    .flatMapIndexed { y, l -> l.mapIndexed { x, s -> if (s != ' ') Pt(x, y) to s.toString() else null } }
    .filterNotNull()
    .toMap()

val maxX = path.maxBy { it.key.x }.key.x
val maxY = path.maxBy { it.key.y }.key.y

val moves = Regex("\\d+|[LR]").findAll(input.last()).map {
    when (it.value) {
        "L" -> Move(dir = Left)
        "R" -> Move(dir = Right)
        else -> Move(it.value.toInt())
    }
}

fun next(p: Pt, dir: Dir) = with(p) {
    when (dir) {
        Right -> if (Pt(x + 1, y) in path) Pt(x + 1, y) else Pt((0..maxX).first { Pt(it, y) in path }, y)
        Left -> if (Pt(x - 1, y) in path) Pt(x - 1, y) else Pt((maxX downTo 0).first { Pt(it, y) in path }, y)
        Up -> if (Pt(x, y - 1) in path) Pt(x, y - 1) else Pt(x, (maxY downTo 0).first { Pt(x, it) in path })
        Down -> if (Pt(x, y + 1) in path) Pt(x, y + 1) else Pt(x, (0..maxY).first { Pt(x, it) in path })
    }
}

fun nextOnCube(p: Pt, dir: Dir) = with(p) {
    when (dir) {
        Right -> if (Pt(x + 1, p.y) in path) Pt(x + 1, p.y) to dir else changeCubeFace(p, dir)
        Left -> if (Pt(x - 1, p.y) in path) Pt(x - 1, p.y) to dir else changeCubeFace(p, dir)
        Up -> if (Pt(x, y - 1) in path) Pt(x, y - 1) to dir else changeCubeFace(p, dir)
        Down -> if (Pt(x, y + 1) in path) Pt(x, y + 1) to dir else changeCubeFace(p, dir)
    }
}

fun changeCubeFace(p: Pt, dir: Dir) = when {
    dir == Right && p.x / 50 == 1 && p.y / 50 == 1 -> Pt(50 + p.y, 49) to Up
    dir == Right && p.x / 50 == 1 && p.y / 50 == 2 -> Pt(149, 149 - p.y) to Left
    dir == Right && p.x / 50 == 2 && p.y / 50 == 0 -> Pt(99, 149 - p.y) to Left
    dir == Right && p.x / 50 == 0 && p.y / 50 == 3 -> Pt(p.y - 100, 149) to Up
    dir == Left && p.x / 50 == 1 && p.y / 50 == 1 -> Pt(p.y - 50, 100) to Down
    dir == Left && p.x / 50 == 1 && p.y / 50 == 0 -> Pt(0, 149 - p.y) to Right
    dir == Left && p.x / 50 == 0 && p.y / 50 == 2 -> Pt(50, 149 - p.y) to Right
    dir == Left && p.x / 50 == 0 && p.y / 50 == 3 -> Pt(p.y - 100, 0) to Down
    dir == Up && p.x / 50 == 1 && p.y / 50 == 0 -> Pt(0, 100 + p.x) to Right
    dir == Up && p.x / 50 == 0 && p.y / 50 == 2 -> Pt(50, p.x + 50) to Right
    dir == Up && p.x / 50 == 2 && p.y / 50 == 0 -> Pt(p.x - 100, 199) to Up
    dir == Down && p.x / 50 == 1 && p.y / 50 == 2 -> Pt(49, 100 + p.x) to Left
    dir == Down && p.x / 50 == 2 && p.y / 50 == 0 -> Pt(99, -50 + p.x) to Left
    dir == Down && p.x / 50 == 0 && p.y / 50 == 3 -> Pt(p.x + 100, 0) to Down
    else -> throw RuntimeException()
}

fun walkPath(startPosition: Pt, startDir: Dir): Int {
    var pos = startPosition
    var dir = startDir
    moves.forEach { move ->
        if (move.dir != null) dir = dir.turn(move.dir)
        else for (i in 0 until move.steps!!) {
            val next = next(pos, dir)
            if (path[next] != "#") pos = next else break
        }
    }
    return 1000 * (pos.y + 1) + 4 * (pos.x + 1) + dir.value
}

fun walkCube(startPosition: Pt, startDir: Dir): Int {
    var pos = startPosition
    var dir = startDir
    moves.forEach { move ->
        if (move.dir != null) dir = dir.turn(move.dir)
        else for (i in 0 until move.steps!!) {
            val (nextPos, nextDir) = nextOnCube(pos, dir)
            if (path[nextPos] != "#") pos = nextPos.also { dir = nextDir } else break
        }
    }
    return 1000 * (pos.y + 1) + 4 * (pos.x + 1) + dir.value
}

fun main() {
    println("part 1: " + walkPath(Pt(50, 0), Right))
    println("part 2: " + walkCube(Pt(50, 0), Right))
}

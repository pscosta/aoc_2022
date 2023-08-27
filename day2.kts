import java.io.File

fun solve1(p1: String, p2: String) = when {
    map[p1] == p2 -> 3 + scores[p2]!!
    p1 == "B" && p2 == "Z" -> 6 + scores[p2]!!
    p1 == "A" && p2 == "Y" -> 6 + scores[p2]!!
    p1 == "C" && p2 == "X" -> 6 + scores[p2]!!
    else -> scores[p2]!!
}

fun solve2(p1: String, p2: String) = when (p2) {
    "X" -> solve1(p1, lose[p1]!!)
    "Y" -> solve1(p1, map[p1]!!)
    else -> solve1(p1, win[p1]!!)
}

val map = mapOf("A" to "X", "B" to "Y", "C" to "Z")
val scores = mapOf("X" to 1, "Y" to 2, "Z" to 3)
val win = mapOf("B" to "Z", "A" to "Y", "C" to "X")
val lose = mapOf("B" to "X", "A" to "Z", "C" to "Y")

fun main() {
    val plays = File("s2.txt").readLines()
        .map { it.split(" ") }

    println("part 1: " + plays.sumOf { solve1(it.first(), it.last()) })
    println("part 2: " + plays.sumOf { solve2(it.first(), it.last()) })
}

import java.io.File

fun main() {
    val lines = File("input/s3.txt").readLines()

    val sum1 = lines
        .map { it.substring(0, (it.length / 2)).toSet().intersect(it.substring((it.length / 2), it.length).toSet()) }
        .sumOf { it.sumOf { c -> prio(c) } }

    val sum2 = lines
        .chunked(3)
        .map { it.first().toSet().intersect(it[1].toSet()).intersect(it.last().toSet()) }
        .sumOf { it.sumOf { c -> prio(c) } }

    println("part 1: $sum1")
    println("part 2: $sum2")
}

fun prio(c: Char) = when {
    c.isUpperCase() -> c.code - 38
    else -> c.code - 96
}

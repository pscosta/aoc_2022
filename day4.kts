import java.io.File

data class Ranges(val a: Int, val b: Int, val c: Int, val d: Int) {
    val contained = (a <= c && d <= b) || (c <= a && d >= b)
    val overlap = contained || (a <= c && b <= d && c <= b) || (c <= a && d <= b && d >= a)
}

fun MatchResult?.ints() = this!!.destructured.toList().map { it.toInt() }
val regex = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)")

fun main() {
    val lines = File("input/s4.txt")
        .readLines()
        .map { regex.find(it).ints().let { (a, b, c, d) -> Ranges(a, b, c, d) } }

    println("part 1: " + lines.count { it.contained })
    println("part 2: " + lines.count { it.overlap })
}

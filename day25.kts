import java.io.File
import kotlin.math.pow

val input = File("s25.txt").readLines().sumOf { toDecimal(it) }

fun toDecimal(snafu: String) = snafu.reversed().mapIndexed { idx, n ->
    "$n".replace(Regex("=$"), "-2").replace(Regex("-$"), "-1").toInt() * 5.0.pow(idx)
}.sumOf { it.toLong() }

fun toSnafu(dec: Long) = generateSequence(dec) { (it + 2) / 5 }.takeWhile { it != 0L }
    .joinToString("") { "${"=-012"[((it + 2) % 5).toInt()]}" }
    .reversed()

fun main() {
    println("part 1: " + toSnafu(input))
}

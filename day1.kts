import java.io.File

fun solve(s: List<String>) = s.fold(mutableListOf(0)) { sum, cal ->
    if (cal.isNotEmpty()) sum[sum.lastIndex] = sum[sum.lastIndex] + cal.toInt()
    else sum.add(0); sum
}

fun main() {
    val input = File("input/s1.txt").readLines()
    println("part 1: " + solve(input).max())
    println("part 2: " + solve(input).sorted().takeLast(3).sum())
}

import java.io.File

data class Pair(var idx: Int, var v: Long)

fun mix(seed: Int, iterations: Int): Long {
    val ints = file.toMutableList()
    val idxMap = ints.onEach { it.v = it.v * seed }.mapIndexed { idx, pair -> idx to pair }.toMap()
    repeat(iterations) {
        idxMap.forEach { (_, v) ->
            val currIdx = ints.indexOf(v)
            ints.removeAt(currIdx)
            ints.add((currIdx + v.v).mod(ints.size), v)
        }
    }
    val zeroIdx = ints.indexOfFirst { it.v == 0L }
    return listOf(1000, 2000, 3000).sumOf { ints[(zeroIdx + it).mod(ints.size)].v }
}

val file = File("s20.txt").readLines()
    .mapIndexed { idx, v -> Pair(idx, v.toLong()) }

fun main() {
    println("part 1: " + mix(1, 1))
    println("part 2: " + mix(811589153, 10))
}
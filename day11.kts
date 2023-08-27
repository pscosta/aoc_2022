import java.io.File

class `🐒`(val items: MutableList<Long>, val op: (Long) -> Long, val div: Long, val t: Int, val f: Int, var sum: Long)

fun monkeys(): List<`🐒`> = File("s11.txt").readLines()
    .chunked(7) {
        val items = it[1].split(": ")[1].split(", ").map { it.toLong() }.toMutableList()
        val div = it[3].split(" by ")[1].toLong()
        val mt = it[4].last().digitToInt()
        val mf = it[5].last().digitToInt()
        val (operator, att) = it[2].split("old ")[1].split(" ")
        val op = when (operator) {
            "*" -> if (att == "old") { i: Long -> i * i } else { i -> i * att.toLong() }
            else -> if (att == "old") { i -> i + i } else { i -> i + att.toLong() }
        }
        `🐒`(items, op, div, mt, mf, 0L)
    }

fun round(mks: List<`🐒`>, times: Int, reduce: (Long) -> Long): Long = repeat(times) {
    mks.forEach { m ->
        repeat(m.items.size) {
            m.items.removeFirst()
                .let { m.op(it) }
                .let(reduce)
                .also { with(m) { if (it % div == 0L) mks[t].items += it else mks[f].items += it } }
                .also { m.sum++ }
        }
    }
}.let {
    mks.sortedBy { it.sum }
        .takeLast(2)
        .map { it.sum }
        .reduce(Long::times)
}

fun main() {
    val lcm = monkeys().map { it.div }.reduce(Long::times)
    println("part 1: " + round(monkeys(), 20) { it / 3 })
    println("part 2: " + round(monkeys(), 10000) { it.mod(lcm) })
}

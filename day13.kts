import java.io.File

data class PList<T : Any>(val parent: PList<T>? = null) : ArrayList<Any>() {
    constructor(item: Int) : this() {
        this.add(item)
    }

    override fun equals(other: Any?) = super.equals(other)
    override fun hashCode() = super.hashCode()
}

val packets = File("s13.txt").readLines()
    .filter { it.isNotEmpty() }
    .map { packet ->
        val rootPacket = PList<Any>()
        var list = rootPacket
        var value = ""
        packet.forEach { c ->
            if (c == '[') {
                list = PList(list)
                list.parent?.add(list)
            } else if (c == ']' || c == ',') {
                if (value.isNotBlank()) list.add(value.toInt()).also { value = "" }
                if (c != ',') list = list.parent!!
            } else value += c
        }
        rootPacket
    }

fun compare(left: Any, right: Any): Int = when {
    left is Int && right is Int -> when {
        left < right -> -1
        left == right -> 0
        else -> 1
    }

    left !is Int && right !is Int -> {
        (left as PList<*>).indices.forEach { i ->
            when (i) {
                !in (right as PList<*>).indices -> return 1
                else -> when (val sorted = compare(left[i], right[i])) {
                    -1, 1 -> return sorted
                }
            }
        }
        if (left.size < (right as PList<*>).size) -1 else 0
    }

    (left !is Int) && right is Int -> compare(left, PList<Any>(right))
    left is Int && (right !is Int) -> compare(PList<Any>(left), right)
    else -> -1
}


fun main() {
    println("part 1: " + packets.chunked(2).mapIndexed { i, (l, r) -> if (compare(l, r) == -1) i + 1 else 0 }.sum())

    val dividers = listOf(PList(2), PList<Any>(6))
    println("Part 2: " + packets.plus(dividers)
        .sortedWith(this::compare)
        .mapIndexed { i, it -> if (dividers.contains(it)) i + 1 else 1 }
        .reduce(Int::times))
}


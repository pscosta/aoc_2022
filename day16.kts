import java.io.File

data class State(val valve: String, val time: Int, val seenValves: Set<Valve>)
data class Valve(val name: String, val rate: Int, val next: List<String>)

val valves = File("s16.txt").readLines()
    .map {
        val name = it.substringAfter("Valve ").substringBefore(" ")
        val flow = it.substringAfter("rate=").substringBefore(";").toInt()
        val edges = it.substringAfter("to valve").substringAfter(" ").split(", ")
        Valve(name, flow, edges)
    }
    .associateBy(Valve::name)

val flowingValves = valves.filter { it.value.rate > 0 }
val start = valves.getValue("AA")
val costs = traverse()

fun traverse(
    time: Int,
    current: Valve = start,
    unseenValves: Set<Valve> = flowingValves.values.toSet(),
    cache: MutableMap<State, Int> = mutableMapOf(),
    proceed: Boolean = false
): Int {
    val elapsedTime = time * current.rate
    val state = State(current.name, time, unseenValves)

    return elapsedTime + cache.getOrPut(state) {
        val maxCurrent = unseenValves
            .filter { next -> costs[current.name]!![next.name]!! < time }
            .takeIf { it.isNotEmpty() }
            ?.maxOf { next ->
                val remainingTime = time - 1 - costs[current.name]!![next.name]!!
                traverse(remainingTime, next, unseenValves - next, cache, proceed)
            } ?: 0
        maxOf(maxCurrent, if (proceed) traverse(time = 26, unseenValves = unseenValves) else 0)
    }
}

fun traverse() = valves.keys.map { valve ->
    val costs = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
    val queue = mutableListOf(valve)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        valves[current]!!.next.forEach { edge ->
            val cost = costs[current]!! + 1
            if (cost < costs.getValue(edge)) {
                costs[edge] = cost
                queue.add(edge)
            }
        }
    }
    costs
}.associateBy { it.keys.first() }

fun main() {
    println("part 1: " + traverse(time = 30))
    println("part 1: " + traverse(time = 26, proceed = true))
}

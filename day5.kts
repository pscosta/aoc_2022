import java.io.File

data class Instruction(val cranes: Int, val from: Int, val to: Int)

val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
var stacks = MutableList(9) { mutableListOf<Char>() }
var instructions = ArrayList<Instruction>()

fun load() {
    instructions = ArrayList()
    stacks = MutableList(9) { mutableListOf() }

    File("s5.txt").readLines()
        .forEach {
            when {
                it.startsWith("[") -> {
                    val line = it.replace("    ", " [0]").replace("[", "").replace("]", "").replace(" ", "")
                    line.mapIndexed { i, c -> if (c != '0') stacks[i].add(c) }
                }
                it.startsWith("move") -> {
                    val (cranes, from, to) = regex.find(it)!!.destructured
                    instructions.add(Instruction(cranes.toInt(), from.toInt() - 1, to.toInt() - 1))
                }
            }
        }
        .also { stacks.map { it.reverse() } }
}

fun move(multiple: Boolean): String {
    instructions.forEach {
        var cranes = stacks[it.from].takeLast(it.cranes)
        if (!multiple) cranes = cranes.reversed()
        stacks[it.from] = stacks[it.from].subList(0, stacks[it.from].size - it.cranes)
        stacks[it.to].addAll(cranes)
    }
    return String(stacks.map { it.last() }.toCharArray())
}

fun main() {
    load().also { println("part 1: " + move(false)) }
    load().also { println("part 2: " + move(true)) }
}
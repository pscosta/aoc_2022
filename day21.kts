import java.io.File

data class Monkey(
    val name: String,
    var number: Long? = null,
    var m1: Monkey? = null,
    var m2: Monkey? = null,
    val op: ((Long, Long) -> Long)? = null,
    var ops: String? = null
)

fun load() = File("s21.txt").readLines().map { l ->
    val regex = Regex("(\\w+): ((\\d+)|((\\w+) (\\*|\\+|\\-|\\/) (\\w+)))$")
    val m = regex.find(l)!!.destructured.toList().filter { it.isNotEmpty() }
    when (m.size) {
        3 -> Monkey(name = m[0], number = m[1].toLong())
        else -> Monkey(name = m[0], m1 = Monkey(name = m[3]), m2 = Monkey(name = m[5]), op = op(m[4]), ops = m[4])
    }
}.associateBy { it.name }

fun op(op: String): (Long, Long) -> Long = when (op) {
    "+" -> { a, b -> a + b }
    "-" -> { a, b -> a - b }
    "/" -> { a, b -> a / b }
    else -> { a, b -> a * b }
}

var monkeys = load()
var nums = monkeys.values.filter { it.number != null }.associate { it.name to it.number!! }.toMutableMap()

fun part1(): Long? {
    while (nums.size < monkeys.size)
        monkeys.values
            .filter { it.number == null }
            .filter { nums.containsKey(it.m1!!.name) && nums.containsKey(it.m2!!.name) }
            .forEach {
                it.number = it.op!!(nums[it.m1!!.name]!!, nums[it.m2!!.name]!!)
                nums[it.name] = it.number!!
            }

    return nums["root"]
}

fun part2(): String {
    monkeys = load()
    nums = monkeys.values
        .filter { it.number != null }
        .filter { it.name != "root" && it.name != "humn" }
        .associate { it.name to it.number!! }.toMutableMap()

    while (true) {
        val size = nums.size
        monkeys.values
            .filter { it.number == null }
            .filter { it.name != "root" && it.name != "humn" }
            .filter { nums.containsKey(it.m1!!.name) && nums.containsKey(it.m2!!.name) }
            .forEach {
                it.number = it.op!!(nums[it.m1!!.name]!!, nums[it.m2!!.name]!!)
                nums[it.name] = it.number!!
            }
        if (size == nums.size) break
    }

    monkeys["root"]!!.ops = "="
    return eval(monkeys["root"]!!)
}

fun eval(m: Monkey): String = when {
    m.name == "humn" -> "X"
    m.number != null -> m.number.toString()
    else -> "(" + eval(monkeys[m.m1!!.name]!!) + " ${m.ops} " + eval(monkeys[m.m2!!.name]!!) + ")"
}

fun main() {
    println("part 1: " + part1())
    println("part 2: " + part2()) // run on wolfram alpha
}

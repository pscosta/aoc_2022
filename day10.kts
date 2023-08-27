import java.io.File

data class OP(val code: String, val att: Int? = null)

class CPU(var strength: Int = 0, var x: Int = 1, var cycle: Int = 0, val hPixels: Int = 40) {
    private val crt = ArrayList<String>()

    fun run(ops: List<OP>, cycles: Set<Int>) = ops.forEach { op ->
        when (cycle % hPixels) {
            x, (x + 1), (x - 1) -> crt.add("#")
            else -> crt.add(" ")
        }
        cycle++
        if (cycles.contains(cycle)) strength += cycle * x
        op.att?.let { x += it }
    }

    fun draw() = crt.chunked(hPixels).map { println(it.joinToString("")) }
}

val ops = mutableListOf<OP>()
File("s10.txt")
    .readLines()
    .map { it.split(" ") }
    .map { op ->
        ops.add(OP(op[0]))
        if (op[0] == "addx") ops.add(OP(op[0], op[1].toInt()))
    }

fun main() {
    val cpu = CPU().also { it.run(ops, setOf(20, 60, 100, 140, 180, 220)) }
    println("part 1: " + cpu.strength)
    println("part 2: " + cpu.draw())
}

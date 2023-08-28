import java.io.File

val jets = File("input/s17.txt").readText().toList()
val cave = mutableMapOf<Int, MutableMap<Int, Any>>()
val caveWidth = 7
val rockTypes = listOf(
    Rock(setOf(Pt(0, 1), Pt(1, 1), Pt(2, 1), Pt(3, 1))), // _
    Rock(setOf(Pt(1, 1), Pt(1, 2), Pt(1, 3), Pt(0, 2), Pt(2, 2))), // +
    Rock(setOf(Pt(0, 1), Pt(1, 1), Pt(2, 1), Pt(2, 2), Pt(2, 3))), // _|
    Rock(setOf(Pt(0, 1), Pt(0, 2), Pt(0, 3), Pt(0, 4))), // |
    Rock(setOf(Pt(0, 1), Pt(1, 1), Pt(0, 2), Pt(1, 2))), // sqr
)

fun Map<*, Map<Int, *>>.isRock(x: Int, y: Int) = x == caveWidth || x < 0 || y == 0 || this[y]?.containsKey(x) == true

data class Pt(var x: Int, var y: Int)
data class Rock(val pts: Set<Pt>) {
    private fun copy() = Rock(pts.map { Pt(it.x, it.y) }.toSet())
    fun newOnTop() = copy().apply { pts.forEach { it.x += 2; it.y += cave.size + 3 } }
    fun canDrop() = pts.none { cave.isRock(it.x, it.y - 1) }
    fun drop() = pts.forEach { it.y-- }
    fun rest() = pts.forEach { cave.getOrPut(it.y) { HashMap() }[it.x] = "#" }
    fun moveRight() = if (pts.none { cave.isRock(it.x + 1, it.y) }) pts.forEach { it.x++ } else null
    fun moveLeft() = if (pts.none { cave.isRock(it.x - 1, it.y) }) pts.forEach { it.x-- } else null
}

fun drop(rockDrops: Int): Int {
    var jetIdx = 0
    for (rockIdx in 0 until rockDrops) {
        val rock = rockTypes[rockIdx % rockTypes.size].newOnTop()
        while (true) {
            when (jets[jetIdx++ % jets.size]) {
                '>' -> rock.moveRight()
                '<' -> rock.moveLeft()
            }
            if (rock.canDrop()) rock.drop() else {
                rock.rest()
                break
            }
        }
    }
    return cave.size
}

fun printCave() = cave.values.reversed().forEachIndexed { i, v ->
    print("$i |").also { (0 until caveWidth).forEach { print(v[it] ?: ".") }.also { println("| - ${v.hashCode()}") } }
}

fun main() {
    println("part1 : " + drop(2022))
    printCave()
}

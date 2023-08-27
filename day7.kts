import java.io.File

data class FileX(val size: Int, val name: String)
data class Dir(
    val name: String,
    val parent: Dir?,
    val files: MutableList<FileX> = mutableListOf(),
    val dirs: MutableList<Dir> = mutableListOf()
) {
    fun size(): Int = files.sumOf { it.size } + dirs.sumOf { it.size() }
}

val root = Dir("/", null, mutableListOf(), mutableListOf())
val dirs = mutableListOf<Dir>()

fun readTerminal(instructions: List<String>) {
    var parent = root
    instructions.forEach {
        when {
            it.startsWith("\$ cd ..") -> parent = parent.parent!!
            it.startsWith("\$ cd") -> {
                val name = it.replace("\$ cd ", "")
                val dir = Dir(name, parent)
                parent.dirs.add(dir)
                parent = dir
                dirs.add(dir)
            }
            !it.startsWith("\$ ls") && !it.startsWith("dir") -> {
                parent.files.add(it.split(" ").let { (size, n) -> FileX(size.toInt(), n) })
            }
        }
    }
}

fun main() {
    val instructions = File("s7.txt").readLines()
    readTerminal(instructions)
    val missing = 30000000 - (70000000 - root.size())

    println("part 1: " + dirs.filter { it.size() <= 100000 }.sumOf { it.size() })
    println("part 2: " + dirs.filter { it.size() >= missing }.minBy { it.size() }.size())
}

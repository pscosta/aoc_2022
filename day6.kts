import java.io.File

fun countChars(data: String, size: Int, step: Int = 1): Int {
    var total = 0
    val set = HashSet<Char>()

    data.windowed(size, 1) {
        if (set.size == size) return@windowed
        var counter = 0

        it.forEach { char ->
            counter++
            set.add(char)
            if (set.size == size) {
                total += counter
                return@windowed
            }
        }
        set.clear()
        total += step
    }
    return total
}

fun main() {
    val data = File("input/s6.txt").readText()
    println("part 1: " + countChars(data, 4))
    println("part 2: " + countChars(data, 14))
}
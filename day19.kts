import java.io.File
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

interface Robot {
    fun build(state: State): State?
    fun requiredTime(state: State): Int
    fun elapse(cost: Int, stock: Int, robots: Int): Int =
        1 + ceil(((cost - stock).coerceAtLeast(0) / robots.toFloat())).toInt()
}

data class OreRobot(val oreCost: Int = 0) : Robot {
    override fun requiredTime(state: State): Int = elapse(oreCost, state.ore, state.oreRobots)
    override fun build(state: State): State? = with(state) {
        val elapsedTime = requiredTime(state)
        return when {
            oreRobots <= 3 && elapsedTime <= state.time -> copy(
                time = state.time - elapsedTime,
                clay = clay + clayRobots * elapsedTime,
                obsidian = obsidian + obsidianRobots * elapsedTime,
                geode = geode + geodeRobots * elapsedTime,
                oreRobots = oreRobots + 1,
                ore = ore + oreRobots * elapsedTime - oreCost,
            )
            else -> null
        }
    }
}

data class ClayRobot(val oreCost: Int = 0) : Robot {
    override fun requiredTime(state: State) = elapse(oreCost, state.ore, state.oreRobots)

    override fun build(state: State): State? = with(state) {
        val elapsedTime = requiredTime(state)
        return when {
            elapsedTime <= state.time -> copy(
                time = state.time - elapsedTime,
                clay = clay + clayRobots * elapsedTime,
                obsidian = obsidian + obsidianRobots * elapsedTime,
                geode = geode + geodeRobots * elapsedTime,
                clayRobots = clayRobots + 1,
                ore = ore + oreRobots * elapsedTime - oreCost
            )
            else -> null
        }
    }
}

data class ObsidianRobot(val oreCost: Int = 0, val clayCost: Int = 0) : Robot {
    override fun requiredTime(state: State): Int = with(state) {
        return maxOf(elapse(oreCost, ore, oreRobots), elapse(clayCost, clay, clayRobots))
    }

    override fun build(state: State): State? = with(state) {
        val elapsedTime = requiredTime(state)
        return when {
            clayRobots > 0 && elapsedTime <= state.time -> copy(
                time = state.time - elapsedTime,
                obsidian = obsidian + obsidianRobots * elapsedTime,
                geode = geode + geodeRobots * elapsedTime,
                obsidianRobots = obsidianRobots + 1,
                ore = ore + oreRobots * elapsedTime - oreCost,
                clay = clay + clayRobots * elapsedTime - clayCost,
            )
            else -> null
        }
    }
}

data class GeodeRobot(val oreCost: Int = 0, val obsidianCost: Int = 0) : Robot {
    override fun requiredTime(state: State): Int = with(state) {
        return maxOf(elapse(oreCost, ore, oreRobots), elapse(obsidianCost, obsidian, obsidianRobots))
    }

    override fun build(state: State): State? = with(state) {
        val elapsedTime = requiredTime(state)
        return when {
            obsidianRobots > 0 && elapsedTime <= state.time -> copy(
                time = state.time - elapsedTime,
                clay = clay + clayRobots * elapsedTime,
                geode = geode + geodeRobots * elapsedTime,
                geodeRobots = geodeRobots + 1,
                ore = ore + oreRobots * elapsedTime - oreCost,
                obsidian = obsidian + obsidianRobots * elapsedTime - obsidianCost,
            )
            else -> null
        }
    }
}

data class State(
    val time: Int,
    val ore: Int = 1,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geode: Int = 0,
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0,
) : Comparable<State> {
    override fun compareTo(other: State) = other.geode.compareTo(geode)
    fun maxPossibleGeodes() = geode + ((0..<time - 1).sumOf { it + geodeRobots })
    fun totalGeodes() = geode + geodeRobots * (time - 1)
}

data class Blueprint(
    val id: Int,
    val oreRobot: OreRobot,
    val clayRobot: ClayRobot,
    val obsidianRobot: ObsidianRobot,
    val geodeRobot: GeodeRobot
)

fun maximizeGeodes(bp: Blueprint, time: Int): Int {
    var maxGeodes = 0
    val sortedQ = PriorityQueue<State>().also { it.add(State(time)) }
    while (sortedQ.isNotEmpty()) {
        val state = sortedQ.poll()
        if (state.maxPossibleGeodes() > maxGeodes) sortedQ.addAll(buildRobots(bp, state))
        maxGeodes = max(maxGeodes, state.totalGeodes())
    }
    return maxGeodes
}

fun buildRobots(blueprint: Blueprint, state: State) = mutableListOf(
    blueprint.oreRobot.build(state),
    blueprint.clayRobot.build(state),
    blueprint.obsidianRobot.build(state),
    blueprint.geodeRobot.build(state)
).filterNotNull()

val regex = Regex("""\d+""")
val bluePrints = File("s19.txt").readLines().map { l ->
    regex.findAll(l).toList().map { it.value }.map(String::toInt).let {
        Blueprint(it[0], OreRobot(it[1]), ClayRobot(it[2]), ObsidianRobot(it[3], it[4]), GeodeRobot(it[5], it[6]))
    }
}

fun main() {
    println("part 1: " + bluePrints.sumOf { it.id * maximizeGeodes(it, 24) })
    println("part 2: " + bluePrints.take(3).map { maximizeGeodes(it, 32) }.reduce(Int::times))
}
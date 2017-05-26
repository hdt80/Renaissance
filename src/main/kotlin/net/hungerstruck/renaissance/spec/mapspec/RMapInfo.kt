package net.hungerstruck.renaissance.spec.mapspec

import net.hungerstruck.renaissance.modules.region.RRegion
import org.bukkit.Difficulty
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * Contains information about the map
 */
data class RMapInfo(
        val name: String,
        val version: String,
        val region: RRegion,
        val center: Vector,
        val lobby: String?,
        val lobbyProperties: RLobbyProperties?,
        val objective: String,
        val authors: Collection<Contributor>,
        val contributors: Collection<Contributor>,
        val rules: Collection<String>,
        val difficulty: Difficulty,
        val dimension: World.Environment) {

    val friendlyDescription: String
        get() = "$name by ${authors.map { it.name }.joinToString(", ")}"
}

data class RLobbyProperties(
        var canBuild: Boolean = false,
        var canTakeDamage: Boolean = false,
        var spawnLoc: Vector = Vector(0, 70, 0)
)

/**
 * Simple contributor data class.
 */
data class Contributor(val name: String, var contribution: String? = null) {
    infix fun who(contrib: String) {
        this.contribution = contrib
    }
}
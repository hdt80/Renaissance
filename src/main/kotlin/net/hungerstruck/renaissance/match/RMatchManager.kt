package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.util.FileUtil
import net.hungerstruck.renaissance.spec.mapspec.RMap
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld
import java.io.File
import java.util.*

/**
 * Manages matches
 */
class RMatchManager() {
    val matches: MutableMap<World, RMatch> = hashMapOf()

    private var matchCount: Int = 0

    fun constructMatch(nextMap: RMap): RMatch {
        val worldName = RConfig.Maps.worldPrefix + ++matchCount

        val worldFolder = File(Bukkit.getServer().worldContainer, worldName)
        if (worldFolder.exists()) {
            worldFolder.deleteRecursively()
        }
        FileUtil.copyWorldFolder(nextMap.location, worldFolder)

        val gen = WorldCreator(worldName).generator(RGenerator()).generateStructures(false).environment(nextMap.mapInfo.dimension)
        val world = Bukkit.createWorld(gen)
        world.isAutoSave = false
        world.difficulty = nextMap.mapInfo.difficulty

        val match = RMatch(matchCount, nextMap, world)
        matches[world] = match

        RLogger.info("Loaded ${nextMap.mapInfo.friendlyDescription}")
        return match
    }

    /**
     * Unload a match, sending all players in in back to a Lobby and removing the folder for the match
     *
     * @param oldMatch Match that will be unloaded
     */
    fun unloadMatch(oldMatch: RMatch) {
        RLogger.debug("Unloading ${oldMatch.map.mapInfo.friendlyDescription}...")

        val dir = oldMatch.world.worldFolder
        matches.remove(oldMatch.world)

        if (oldMatch.world is CraftWorld) {
            if (oldMatch.world.handle.players.size > 0) {
                RLogger.warn("${oldMatch.map.mapInfo.name} still has players in it. Forcing a unload")

                for (player in oldMatch.world.players) {
                    Renaissance.lobbyManager.defaultLobby.join(player.rplayer, true)
                    player.kickPlayer("You have not respawned so the game cannot exit")
                }
            }
        }

        if (!Bukkit.unloadWorld(oldMatch.world, false)) {
            RLogger.error("Failed to unload ${oldMatch.map.mapInfo.name}. unloadWorld() returned false")
            return
        }
        FileUtil.delete(dir)

        RLogger.info("Unloaded ${oldMatch.map.mapInfo.friendlyDescription}")
    }

    // Note: May return null if there are no active matches.
    fun findMatch(strategy: RConfig.JoinStrategy): RMatch? {
        if (matches.isEmpty()) return null

        return when (strategy) {
            RConfig.JoinStrategy.FIRST -> matches.values.first()
            RConfig.JoinStrategy.RANDOM -> matches.values.toList()[Random().nextInt(matches.size)]
            RConfig.JoinStrategy.SMALLEST -> matches.values.minBy { it.players.size }
        }
    }
}
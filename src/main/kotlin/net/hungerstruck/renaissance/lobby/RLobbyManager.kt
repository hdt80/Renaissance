package net.hungerstruck.renaissance.lobby

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.match.RGenerator
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File

class RLobbyManager(lobbyName: String) {
    var defaultLobby: RLobby

    init {
        val lobbyFolder = File(Bukkit.getServer().worldContainer, lobbyName)
        if (!lobbyFolder.exists()) {
            throw IllegalStateException("The lobby folder does not exist")
        }

        val gen = WorldCreator(lobbyName).generator(RGenerator()).generateStructures(false).environment(World.Environment.NORMAL)
        val lobbyWorld = Bukkit.createWorld(gen)
        lobbyWorld.isAutoSave = false
        lobbyWorld.difficulty = Difficulty.PEACEFUL

        defaultLobby = RLobby(lobbyWorld, Renaissance.mapContext.getMapExact(lobbyName)!!)

        RLogger.info("Default lobby \"$lobbyName\" has been loaded")
    }

    /**
     * Check if a world is the same as the defaultLobby's world
     *
     * returns: defaultLobby if the world is the same as defaultLobby's, or null if not a match
     */
    fun isLobbyWorld(world: World): RLobby? {
        if (defaultLobby.lobbyWorld == world) {
            return defaultLobby
        }
        return null
    }

}
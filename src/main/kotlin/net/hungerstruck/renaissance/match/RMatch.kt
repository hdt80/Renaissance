package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.event.match.RMatchEndEvent
import net.hungerstruck.renaissance.event.match.RMatchLoadEvent
import net.hungerstruck.renaissance.event.match.RMatchStartEvent
import net.hungerstruck.renaissance.xml.RMap
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Bukkit
import org.bukkit.World

/**
 * Represents a match.
 *
 * Created by molenzwiebel on 20-12-15.
 */
class RMatch {
    val map: RMap
    val world: World
    var state: State = State.LOADED

    private val moduleContext: RModuleContext

    val players: List<RPlayer>
        get() = RPlayer.getPlayers() { it.match == this }

    val alivePlayers: List<RPlayer>
        get() = RPlayer.getPlayers() { it.match == this && it.state == RPlayer.State.PARTICIPATING }

    constructor(map: RMap, world: World) {
        this.map = map
        this.world = world

        this.moduleContext = RModuleContext(this, map.document)

        Bukkit.getPluginManager().callEvent(RMatchLoadEvent(this))
    }

    public fun sendMessage(msg: String, f: (RPlayer) -> Boolean = { true }) {
        players.filter(f).forEach { it.sendMessage(msg) }
    }

    /**
     * Begins the starting countdown for this match.
     */
    public fun beginCountdown() {
        assert(state == State.LOADED, { "Cannot begin countdown from state $state" })
        state = State.STARTING
        //FIXME: Don't hardcode 10s
        Renaissance.countdownManager.start(RMatchStartCountdown(this), 10)
    }

    /**
     * Starts the match.
     */
    public fun startMatch() {
        state = State.PLAYING
        Bukkit.getPluginManager().callEvent(RMatchStartEvent(this))
    }

    /**
     * Ends the match
     */
    public fun endMatch() {
        state = State.ENDED
        Bukkit.getPluginManager().callEvent(RMatchEndEvent(this))
    }

    /**
     * Performs any unloading and cleanup that this map might want to do.
     */
    public fun cleanup() {
        for (module in moduleContext.modules) {
            module.cleanup()
        }
    }

    public enum class State {
        // Loaded. Players are not in already, they are still in the lobby for this match.
        LOADED,
        // Countdown for start is running, players are in.
        STARTING,
        // The match is currently in progress.
        PLAYING,
        // The match has ended but has not yet been unloaded. When unloaded, the RMatch gets gcd.
        ENDED;
    }
}
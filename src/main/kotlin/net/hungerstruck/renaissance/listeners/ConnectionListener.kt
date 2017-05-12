package net.hungerstruck.renaissance.listeners

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.event.player.RPlayerJoinMatchEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.rplayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onPlayerConnect(event: PlayerJoinEvent) {
        // Don't broadcast any join message
        event.joinMessage = null

        // First try to place the joining place into a running match
        val match = Renaissance.matchManager.findMatch(RConfig.Match.joinStrategy)
        if (match != null) {
            event.player.rplayer.match = match
            Bukkit.getPluginManager().callEvent(RPlayerJoinMatchEvent(event.player.rplayer, match))
            return
        }

        // If no match is currently running send them to the lobby
        Renaissance.lobbyManager.defaultLobby.join(event.player.rplayer)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // Don't broadcast any quit message
        event.quitMessage = null

        // If they were in a match remove them from it
        if (event.player.rplayer.match != null) {
            val match = event.player.rplayer.match

            if (match?.state == RMatch.State.PLAYING && event.player.rplayer.state == RPlayer.State.PARTICIPATING) {
                event.player.health = 0.0
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerKick(event: PlayerKickEvent) {
        // Don't broadcast any kick message
        event.leaveMessage = null

        // If they were in a match remove them from it
        if (event.player.rplayer.match != null) {
            val match = event.player.rplayer.match

            if (match?.state == RMatch.State.PLAYING && event.player.rplayer.state == RPlayer.State.PARTICIPATING) {
                event.player.health = 0.0
            }
        }
    }
}